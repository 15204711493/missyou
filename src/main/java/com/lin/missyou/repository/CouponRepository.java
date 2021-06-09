package com.lin.missyou.repository;

import com.lin.missyou.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Query("select c from Coupon c " +
            "join c.categoryList ca " +
            "join Activity a on a.id = c.activityId " +
            "where ca.id = :cid " +
            "and a.startTime <:now " +
            "and a.endTime>:now")
    List<Coupon> findByCategory(Long cid, Date now);


   @Query("select c from Coupon c " +
           "join Activity a on a.id = c.activityId " +
           "where c.wholeStore=:b and a.startTime<:date and a.endTime>:date")
    List<Coupon> findByWholeStore(Boolean b,Date date);





//    @Query("select c from Coupon c " +
//            "join UserCoupon uc on c.id = uc.couponId " +
//            "where uc.userId = :uid " +
//            "and uc.status = 1 " +
//            "and c.startTime<:now " +
//            "and c.endTime>:now " +
//            "and uc.orderId is null ")
    @Query("select c from Coupon c " +
            "join UserCoupon uc on uc.couponId = c.id " +
            "join User u on u.id = uc.userId " +
            "where u.id = :uid " +
            "and uc.status = 1 " +
            "and c.startTime<:now " +
            "and c.endTime>:now " +
            "and uc.orderId is null ")
    List<Coupon> findMyAvailable(Long uid,Date now);

    @Query("select c from  Coupon c " +
            "join UserCoupon uc on uc.couponId = c.id " +
            "join User u on u.id = uc.userId " +
            "where u.id = :uid " +
            "and uc.status = 2 " +
            "and c.endTime>:now " +
            "and c.startTime<:now " +
            "and uc.orderId is not null ")
    List<Coupon> findMyUsed(Long uid,Date now);


    @Query("select c from  Coupon c " +
            "join UserCoupon uc on uc.couponId = c.id " +
            "join User u on u.id = uc.userId " +
            "where u.id = :uid " +
            "and uc.status <> 2 " +
            "and c.endTime <:now " +
            "and uc.orderId is  null ")
    List<Coupon> findMyMyExpired(Long uid,Date now);



}
