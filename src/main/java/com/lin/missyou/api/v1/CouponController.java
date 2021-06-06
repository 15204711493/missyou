package com.lin.missyou.api.v1;

import com.lin.missyou.model.Coupon;
import com.lin.missyou.service.CouponService;
import com.lin.missyou.vo.CouponPureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/by/category/{cid}")
    public List<CouponPureVo> getCouponListByCategory(@PathVariable Long cid){

        List<Coupon> couponList = couponService.getCouponList(cid);
        if (couponList.isEmpty()){
            return Collections.emptyList();
        }
        List<CouponPureVo> list = CouponPureVo.getList(couponList);
        return list;

    }

}
