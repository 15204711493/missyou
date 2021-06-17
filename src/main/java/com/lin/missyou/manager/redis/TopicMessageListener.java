package com.lin.missyou.manager.redis;

import com.lin.missyou.bo.OrderMessageBo;
import com.lin.missyou.service.CouponBackService;
import com.lin.missyou.service.OrderCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class TopicMessageListener  implements MessageListener {

    @Autowired
    private OrderCancelService orderCancelService;

    @Autowired
    private CouponBackService couponBackService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        String expiredKey = new String(body);
        String topic = new String(channel);


        OrderMessageBo orderMessageBo = new OrderMessageBo(expiredKey);
        orderCancelService.cancel(orderMessageBo);
        couponBackService.returnBack(orderMessageBo);
    }
}
