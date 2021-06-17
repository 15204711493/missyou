package com.lin.missyou.repository;

import com.lin.missyou.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {

    Optional<UserCoupon> findFirstByUserIdAndAndCouponId(Long uid,Long couponId);

    Optional<UserCoupon> findFirstByUserIdAndAndCouponIdAndStatus(Long uid,Long couponId,int status);

    @Modifying
    @Query("update UserCoupon uc set uc.status = 2," +
            "uc.orderId = :oid " +
            "where uc.userId = :uid " +
            "and uc.couponId = :couponId " +
            "and uc.status = 1 " +
            "and uc.orderId is null ")
    int writeOff(Long couponId,Long oid,Long uid);



    @Modifying
    @Query("update UserCoupon uc set uc.status = 1,uc.orderId = null " +
            "where uc.userId = :uid " +
            "and uc.couponId = :couponId " +
            "and uc.orderId is not null " +
            "and uc.status = 2")
    int returnBack(Long uid,Long couponId);
}
