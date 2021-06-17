package com.lin.missyou.service;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.logic.CouponChecker;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.*;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.until.CommonUntil;
import com.lin.missyou.until.OrderUtil;
import com.lin.missyou.vo.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.ranges.Range;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IMoneyDiscount iMoneyDiscount;

    @Value("${missyou.order.max-sku-limit}")
    private Integer missyouOrderMaxSkuLimit;

    @Value("${missyou.order.pay-time-limit}")
    private Integer payTimeLimit;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    public Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker) {
        String orderNo = OrderUtil.makeOrderNo();
        Calendar now = Calendar.getInstance();
        Calendar now1 = (Calendar) now.clone();

        Order order = Order.builder()
                .orderNo(orderNo)
                .totalPrice(orderDTO.getTotalPrice())
                .finalTotalPrice(orderDTO.getFinalTotalPrice())
                .userId(uid)
                .totalCount(orderChecker.getTotalCount().longValue())
                .snapImg(orderChecker.getLeaderImg())
                .snapTitle(orderChecker.getLeaderTitle())
                .expiredTime(CommonUntil.addSomeSeconds(now, payTimeLimit).getTime())
                .placedTime(now1.getTime())
                .status(OrderStatus.UNPAID.value())
                .build();

        order.setSnapAddress(order.getSnapAddress());
        order.setSnapItems(orderChecker.getOrderSkuList());

        orderRepository.save(order);
        this.reduceStock(orderChecker);

        Long couponId = -1L;
        if (orderDTO.getCouponId() != null) {
            this.writeOffCoupon(orderDTO.getCouponId(), order.getId(), uid);
            couponId = orderDTO.getCouponId();
        }
        this.sendToRedis(order.getId(),uid,couponId);
        return order.getId();
    }

    private void sendToRedis(Long oid,Long uid,Long couponId){
        String key = oid.toString()+","+uid.toString()+","+couponId.toString();
        try {
            stringRedisTemplate.opsForValue().set(key,"1",this.payTimeLimit, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Optional<Order> getOrder(Long id){
        Long uid = LocalUser.getUser().getId();
        Optional<Order> firstByUserIdAndId = this.orderRepository.findFirstByUserIdAndId(uid, id);
        return firstByUserIdAndId;

    }

    public Page<Order> getUnpaid(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        return this.orderRepository.findByExpiredTimeGreaterThanAndStatusAndUserId(new Date(),OrderStatus.UNPAID.value(),uid, pageable);

    }

    public Page<Order> getByStatus(Integer status,Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        if(status == OrderStatus.All.value()){
          return this.orderRepository.findByUserId(uid, pageable);
        }
        return this.orderRepository.findByUserIdAndStatus(uid,status,pageable);
    }

    private void writeOffCoupon(Long couponId, Long orderId, Long uid) {
        int result = userCouponRepository.writeOff(couponId, orderId, uid);
        if (result != 1) {
            throw new ForbiddenException(40012);
        }
    }

    private void reduceStock(OrderChecker orderChecker) {
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for (OrderSku orderSku : orderSkuList) {
            int result = skuRepository.reduceStock(orderSku.getId(), orderSku.getCount().longValue());
            if (result != 1) {
                throw new ParameterException(50003);
            }
        }
    }


    public OrderChecker isOk(Long uid, OrderDTO orderDTO) {
        if (orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0) {
            throw new ParameterException(50001);
        }
        List<Long> ids = orderDTO.getSkuInfoList()
                .stream().map(SkuInfoDTO::getId
                ).collect(Collectors.toList());

        List<Sku> skuList = skuService.getSkuListByIds(ids);

        Long couponId = orderDTO.getCouponId();

        CouponChecker couponChecker = null;

        if (couponId != null) {

            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundEcxeption(40004));

            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndAndCouponIdAndStatus(uid, couponId, 1)
                    .orElseThrow(() -> new NotFoundEcxeption(50006));

            couponChecker = new CouponChecker(coupon, iMoneyDiscount);
        }

        OrderChecker orderChecker = new OrderChecker(
                orderDTO, skuList, couponChecker, missyouOrderMaxSkuLimit);
        orderChecker.isOk();
        return orderChecker;
    }


    public void updateOrderPrepayId(Long orderId, String prePayId){
        Optional<Order> order = orderRepository.findById(orderId);
        order.ifPresent(o->{
            o.setPrepayId(prePayId);
            this.orderRepository.save(o);
        });
        order.orElseThrow(()->new ParameterException(10007));
    }



}
