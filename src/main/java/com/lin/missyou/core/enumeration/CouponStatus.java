package com.lin.missyou.core.enumeration;

public enum  CouponStatus {

    AVAILABLE(1,"可以使用"),
    USED(2,"已使用"),
    EXPIRED(3,"未使用已过期");

    private Integer value;

    public Integer getValue(){
        return this.value;
    }

    CouponStatus(Integer value,String description){
        this.value = value;
    }
}
