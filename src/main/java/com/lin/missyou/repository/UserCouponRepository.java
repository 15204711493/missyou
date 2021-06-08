package com.lin.missyou.repository;

import com.lin.missyou.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {

    Optional<UserCoupon> findFirstByUserIdAndAndCouponId(Long uid,Long couponId);

    @Query("select c from Coupon c " +
            "join UserCoupon  uc on uc.userId = :uid " +
            "where uc.status = :status")
    List<UserCoupon> findByUserIdAndStatus(Long uid,Integer status);

}
