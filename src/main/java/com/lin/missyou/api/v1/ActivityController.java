package com.lin.missyou.api.v1;

import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.model.Activity;
import com.lin.missyou.service.ActivityService;
import com.lin.missyou.vo.ActivityCouponVo;
import com.lin.missyou.vo.ActivityPureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/name/{name}")
    public ActivityPureVo getHomeActivity(@PathVariable String name){
        Activity activity = activityService.getActivity(name);
        if(activity == null){
            throw new NotFoundEcxeption(40001);
        }
        ActivityPureVo activityPureVo = new ActivityPureVo(activity);
        return activityPureVo;

    }


    @GetMapping("/name/{name}/with_coupon")
    public ActivityCouponVo getActivityWithCoupons(@PathVariable String name){
        Activity activity = activityService.getActivity(name);
        if(activity == null){
            throw new NotFoundEcxeption(40001);
        }

        ActivityCouponVo activityCouponVo = new ActivityCouponVo(activity);
        return activityCouponVo;
    }
}
