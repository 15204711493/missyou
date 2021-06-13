package com.lin.missyou.vo;

import com.lin.missyou.model.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
public class OrderPureVo extends Order{
    private Long period;
    private Date createTime;

    public OrderPureVo(Order order,Long period){
        BeanUtils.copyProperties(order,this);
        this.period =period;
    }
}
