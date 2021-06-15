package com.lin.missyou.repository;

import com.lin.missyou.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByExpiredTimeGreaterThanAndStatusAndUserId(Date now, Integer status, Long uid, Pageable pageable);


    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long uid, Integer start, Pageable pageable);

    Optional<Order> findFirstByUserIdAndId(Long uid,Long oid);

    Optional<Order> findFirstByOrderNo(String orderNo);


    @Modifying
    @Query("update Order o set o.status = :status WHERE o.orderNo = :orderNo")
    int updateStatusByOrderNo(String orderNO,Integer status);

}