package com.lin.missyou.service;

import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private SkuService skuService


    public void  isOk(Long uid, OrderDTO orderDTO){
        if(orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0){
            throw  new ParameterException(50001);
        }
        List<Long> ids = orderDTO.getSkuInfoList()
                .stream().map(SkuInfoDTO::getId
                ).collect(Collectors.toList());

        List<Sku> skuList =skuService.getSkuListByIds(ids);
    }
}
