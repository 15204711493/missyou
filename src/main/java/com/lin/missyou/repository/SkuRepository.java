package com.lin.missyou.repository;

import com.lin.missyou.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku,Long> {

    List<Sku> findAllByIdIn (List<Long> ids);

    @Modifying
    @Query("update Sku s set s.stock = s.stock - :quantity " +
            "where s.id = :skuId " +
            "and s.stock >= :quantity")
    int reduceStock(Long skuId,Long quantity);

}
