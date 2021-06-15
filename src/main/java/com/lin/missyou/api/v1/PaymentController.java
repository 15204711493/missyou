package com.lin.missyou.api.v1;

import com.lin.missyou.core.interceptors.ScopeLevel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.util.Map;

@RestController
@RequestMapping("payment")
@Validated
public class PaymentController {

    @PostMapping("/pay/order/{id}")
    @ScopeLevel
    public Map<String,String> payWxOrder(@PathVariable(name = "id") @Positive Long oid){

    }

    @RequestMapping("wx/notify")
    public String payCallback(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse){
        return null;

    }
}