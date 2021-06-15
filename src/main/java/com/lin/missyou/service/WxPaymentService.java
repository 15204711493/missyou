package com.lin.missyou.service;

import com.github.wxpay.sdk.LinWxPayConfigSamp;
import com.github.wxpay.sdk.WXPay;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.exception.http.ServiereErrorException;
import com.lin.missyou.model.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.until.CommonUntil;
import com.lin.missyou.until.HttpRequestProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxPaymentService {
    @Autowired
    private OrderRepository orderRepository;

    @Value("${missyou.order.pay-callback-host}")
    private String PayCallbackHost;

    @Value("${missyou.order.pay-callback-path}")
    private String PayCallBackPath;


    private static LinWxPayConfigSamp linWxPayConfigSamp = new LinWxPayConfigSamp();

    public Map<String, String> preOrder(Long id) {
        Long uid = LocalUser.getUser().getId();
        Optional<Order> optionalOrder = orderRepository.findFirstByUserIdAndId(uid, id);
        Order order = optionalOrder.orElseThrow(() -> {
            throw new NotFoundEcxeption(50009);
        });
        if (order.needCancel()) {
            throw new ForbiddenException(50010);
        }

        WXPay wxPay = this.assembleWxPayConfig();
        Map<String, String> map = this.makePreOrderParams(order.getFinalTotalPrice(), order.getOrderNo());
        try {
            wxPay.unifiedOrder(map);
        } catch (Exception e) {
           throw new ServiereErrorException(9999);
        }

    }

    private Map<String, String> makePreOrderParams(BigDecimal serverFinalPrice, String orderNo) {
        String path = this.PayCallBackPath + this.PayCallBackPath;
        Map<String, String> data = new HashMap<>();
        data.put("body", "Sleeve");
        data.put("out-trade_no", orderNo);
        data.put("device_info", "Sleeve");
        data.put("free_type", "CNY");
        data.put("trade_type", "JSAPI");

        data.put("total-fee", CommonUntil.yuanToFenPlainString(serverFinalPrice));
        data.put("open_id", LocalUser.getUser().getOpenid());
        data.put("spbill_create_ip", HttpRequestProxy.getRemoteRealIp());
        data.put("notify_url", path);
        return data;
    }


    private WXPay assembleWxPayConfig(){
        WXPay wxPay = null;
        try {
            wxPay = new WXPay(WxPaymentService.linWxPayConfigSamp);
        } catch (Exception e) {
            throw new ServiereErrorException(9999);
        }
        return wxPay;
    }
}
