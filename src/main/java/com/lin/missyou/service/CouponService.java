package com.lin.missyou.service;

import com.lin.missyou.model.Coupon;
import com.lin.missyou.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;


    public List<Coupon> getCouponList(Long cid){
        Date now = new Date();
        return couponRepository.findByCategory(cid,now);
    }


    public List<Coupon> getWholeStoreCoupons(){
       return couponRepository.findByWholeStore(true,new Date());
    }



    public void collectOneCoupon(Long uid,Long couponId){


    }


}
