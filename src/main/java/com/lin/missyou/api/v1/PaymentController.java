package com.lin.missyou.api.v1;

import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.lib.LinWxNotify;
import com.lin.missyou.service.WxPaymentNotifyService;
import com.lin.missyou.service.WxPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("payment")
@Validated
public class PaymentController {

    @Autowired
    private WxPaymentService wxPaymentService;

    @Autowired
    private WxPaymentNotifyService wxPaymentNotifyService;

    @PostMapping("/pay/order/{id}")
    @ScopeLevel
    public Map<String, String> payWxOrder(@PathVariable(name = "id") @Positive Long oid) {
        return wxPaymentService.preOrder(oid);
    }

    @RequestMapping("wx/notify")
    public String payCallback(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse) {

        InputStream s;
        try {
            s = httpServletRequest.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return LinWxNotify.fail();
        }

        String xml;
        xml = LinWxNotify.readNotify(s);

        try {
            wxPaymentNotifyService.processPayNotify(xml);
        }catch (Exception e){
            return LinWxNotify.fail();
        }
        return LinWxNotify.success();
    }
}
