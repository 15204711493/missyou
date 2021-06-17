package com.lin.missyou.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMessageBo {

    private Long orderId;
    private Long couponId;
    private Long userId;
    private String message;


    public OrderMessageBo(String message){
        this.message = message;
        this.parseId(message);
    }

    private void parseId(String message){
        String[] temp = message.split(",");
        this.orderId = Long.valueOf(temp[0]);
        this.couponId = Long.valueOf(temp[2]);
        this.userId = Long.valueOf(temp[1]);
    }

}
