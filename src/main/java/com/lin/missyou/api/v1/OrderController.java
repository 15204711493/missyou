package com.lin.missyou.api.v1;

import com.lin.missyou.bo.PageCounter;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.Order;
import com.lin.missyou.service.OrderService;
import com.lin.missyou.until.CommonUntil;
import com.lin.missyou.vo.OrderIdVo;
import com.lin.missyou.vo.OrderPureVo;
import com.lin.missyou.vo.OrderSimplifyVo;
import com.lin.missyou.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${missyou.order.pay-time-limit}")
    private Long pagTimeLimit;

    private static RuntimeException get() {
        throw new NotFoundEcxeption(50009);
    }


    @ScopeLevel
    @PostMapping("")
    public OrderIdVo placeOrder(@RequestBody OrderDTO orderDTO){
        Long uid = LocalUser.getUser().getId();
        System.out.println(uid);
        OrderChecker ok = this.orderService.isOk(uid, orderDTO);
        Long oId = this.orderService.placeOrder(uid, orderDTO, ok);
        return new OrderIdVo(oId);
    }



    @ScopeLevel
    @GetMapping("/status/unpaid")
    @SuppressWarnings("unchecked")
    public PagingDozer getUnpaid(@RequestParam(defaultValue ="0") Integer start,
                                  @RequestParam(defaultValue = "10") Integer count){

        PageCounter page = CommonUntil.convertToPageParameter(start, count);
        Page<Order> unpaid = this.orderService.getUnpaid(page.getPage(), page.getCount());
        PagingDozer pagingDozer = new PagingDozer<>(unpaid, OrderSimplifyVo.class);
        pagingDozer.getItems().forEach(o->((OrderSimplifyVo)o).setPeriod(this.pagTimeLimit));
        return  pagingDozer;
    }




    @ScopeLevel
    @GetMapping("/by/status/{status}")
    public PagingDozer getByStatus(@PathVariable int status,
                                   @RequestParam(defaultValue = "0") Integer start,
                                   @RequestParam(defaultValue = "10") Integer count){
        PageCounter page = CommonUntil.convertToPageParameter(start, count);
        Page<Order> order = this.orderService.getByStatus(status, page.getPage(), page.getCount());
        PagingDozer<Order, OrderSimplifyVo> pagingDozer = new PagingDozer<>(order, OrderSimplifyVo.class);
        pagingDozer.getItems().forEach(o->((OrderSimplifyVo)o).setPeriod(this.pagTimeLimit));
        return pagingDozer;
    }


    @ScopeLevel
    @GetMapping("detail/{id}")
    public OrderPureVo getOrderDetail(@PathVariable Long id){
        Optional<Order> order = this.orderService.getOrder(id);
        return order.map((o)->new OrderPureVo(o,this.pagTimeLimit))
                .orElseThrow(OrderController::get);
    }

}
