package com.lin.missyou.vo;

import com.lin.missyou.model.Activity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActivityCouponVo extends ActivityPureVo{

    private List<CouponPureVo> coupons;



    public ActivityCouponVo(Activity activity){
        super(activity);
        coupons = activity.getCouponList().stream().map(CouponPureVo::new).collect(Collectors.toList());

    }

}
