package com.lin.missyou.vo;

import com.lin.missyou.model.Coupon;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CouponPureVo {
    private Long id;
    private String title;
    private Date startTime;
    private Date endTime;
    private String description;
    private BigDecimal fullMoney;
    private BigDecimal minus;
    private BigDecimal rate;
    private Integer type;
    private String remark;
    private Boolean wholeStore;

//    public CouponPureVO(Object[] objects){
//        Coupon coupon = (Coupon) objects[0];
//        BeanUtils.copyProperties(coupon, this);
//    }
    public CouponPureVo(Coupon coupon){
        BeanUtils.copyProperties(coupon,this);
    }

    public static List<CouponPureVo> getList(List<Coupon> couponList){
        List<CouponPureVo> collect = couponList.stream().map(CouponPureVo::new).collect(Collectors.toList());
        return collect;
    }

}
