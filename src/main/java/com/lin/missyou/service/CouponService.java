package com.lin.missyou.service;

import com.lin.missyou.core.enumeration.CouponStatus;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Activity;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.repository.ActivityRepository;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.repository.UserRepository;
import com.lin.missyou.until.CommonUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    public List<Coupon> getCouponList(Long cid) {
        Date now = new Date();
        return couponRepository.findByCategory(cid, now);
    }


    public List<Coupon> getWholeStoreCoupons() {
        return couponRepository.findByWholeStore(true, new Date());
    }


    public void collectOneCoupon(Long uid, Long couponId) {

        Optional<Coupon> byId = couponRepository.findById(couponId);
        byId.orElseThrow(() -> new NotFoundEcxeption(40003));

        Activity activity = activityRepository.findByCouponListId(couponId)
                .orElseThrow(() -> new NotFoundEcxeption(40010));

        Boolean inTimeLine =
                CommonUntil.isInTimeLine(new Date(), activity.getStartTime(), activity.getEndTime());
        if (!inTimeLine) {
            throw new ParameterException(40005);
        }

        userCouponRepository.findFirstByUserIdAndAndCouponId(uid, couponId)
                .ifPresent((uc)->{throw  new ParameterException(40006);});

        UserCoupon userCoupon = UserCoupon.builder()
                .userId(uid)
                .couponId(couponId)
                .status(CouponStatus.AVAILABLE.getValue())
                .createTime(new Date())
                .build();
        userCouponRepository.save(userCoupon);


    }


}
