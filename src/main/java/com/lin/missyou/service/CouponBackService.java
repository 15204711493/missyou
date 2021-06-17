package com.lin.missyou.service;

import com.lin.missyou.bo.OrderMessageBo;


import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.exception.http.ServiereErrorException;
import com.lin.missyou.model.Order;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CouponBackService {
    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void returnBack(OrderMessageBo messageBo) {
        Long couponId = messageBo.getCouponId();
        Long userId = messageBo.getUserId();
        Long orderId = messageBo.getOrderId();
        if (couponId == -1) {
            return;
        }

        Optional<Order> orderOptional =
                orderRepository.findFirstByUserIdAndId(userId, orderId);
        Order order = orderOptional.orElseThrow(() -> {
            throw new ServiereErrorException(9999);
        });

        if (order.getStatusEnum().equals(OrderStatus.UNPAID)
                || order.getStatusEnum().equals(OrderStatus.CANCELED)) {
           this.userCouponRepository.returnBack(userId, couponId);

        }

    }

}
