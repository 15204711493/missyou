package com.lin.missyou.api.v1;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.UnifyResponse;
import com.lin.missyou.core.enumeration.CouponStatus;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.exception.CreateSuccess;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.User;
import com.lin.missyou.service.CouponService;
import com.lin.missyou.service.UserService;
import com.lin.missyou.vo.CategoryPureVo;
import com.lin.missyou.vo.CouponCategoryVo;
import com.lin.missyou.vo.CouponPureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;


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
    @PostMapping("/collect/{id}")
    public void collectCoupon(@PathVariable Long id) {
        //从拦截器中获取用户的id
        Long userId = LocalUser.getUser().getId();
        couponService.collectOneCoupon(userId, id);
        UnifyResponse.createSuccess(0);
    }


    @ScopeLevel
    @GetMapping("/myself/by/status/{status}")
    public List<CouponPureVo> geyMyCountByStatus(@PathVariable Integer status) {
        Long uid = LocalUser.getUser().getId();

        List<Coupon> couponList = null;
        switch (CouponStatus.toType(status)) {
            case AVAILABLE:
                couponList = couponService.getMyAvailableCoupons(uid);
                break;
            case USED:
                couponList = couponService.getMyUsedCoupons(uid);
                break;
            case EXPIRED:
                couponList = couponService.getMyExpiredCoupons(uid);
                break;
            default:
                throw new ParameterException(40001);
        }
        return CouponPureVo.getList(couponList);
    }


    @ScopeLevel
    @GetMapping("/myself/available/with_category")
    public List<CouponCategoryVo> getUserCouponWithCategory(){
        Long uid = LocalUser.getUser().getId();
        List<Coupon> coupons = couponService.getMyAvailableCoupons(uid);
        if(coupons.isEmpty()){
            return Collections.emptyList();
        }
        return coupons.stream().map(coupon -> {
            CouponCategoryVo couponCategoryVo = new CouponCategoryVo(coupon);
            return couponCategoryVo;
        }).collect(Collectors.toList());
    }


}
