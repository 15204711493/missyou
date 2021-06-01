package com.lin.missyou.repository;

import com.lin.missyou.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface BannerRepository extends JpaRepository<Banner,Long> {
    Banner findOneByName(String name);
}
