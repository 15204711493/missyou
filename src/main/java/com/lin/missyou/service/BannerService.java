package com.lin.missyou.service;


import com.lin.missyou.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerService {
    Banner getByName(String name);
}
