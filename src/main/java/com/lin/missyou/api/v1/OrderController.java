package com.lin.missyou.api.v1;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.dto.OrderDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("order")
@Validated
public class OrderController {


    @ScopeLevel
    @PostMapping("")
    public OrderIdVo placeOrder(@RequestBody OrderDTO orderDTO){
        Long uid = LocalUser.getUser().getId();


    }

}
