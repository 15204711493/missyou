package com.lin.missyou.api.v1;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.User;
import com.lin.missyou.service.CouponService;
import com.lin.missyou.service.UserService;
import com.lin.missyou.vo.CouponPureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserService userService;


    @GetMapping("/by/category/{cid}")
    public List<CouponPureVo> getCouponListByCategory(@PathVariable Long cid) {

        List<Coupon> couponList = couponService.getCouponList(cid);
        if (couponList.isEmpty()) {
            return Collections.emptyList();
        }
        List<CouponPureVo> list = CouponPureVo.getList(couponList);
        return list;

    }


    @GetMapping("/whole_store")
    public List<CouponPureVo> getWholeStoreCouponList() {
        List<Coupon> wholeStoreCoupons = couponService.getWholeStoreCoupons();
        if (wholeStoreCoupons.isEmpty()) {
            return Collections.emptyList();
        }

        return CouponPureVo.getList(wholeStoreCoupons);

    }

    @ScopeLevel
    @GetMapping("/collect/{id}")
    public void  collectCoupon(@PathVariable Long id){
        Long userId = LocalUser.getUser().getId();



    }


}
