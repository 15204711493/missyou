package com.lin.missyou.logic;


import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Sku;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderChecker {

    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponChecker couponChecker;
    private Integer maxSkuLimit;

    public OrderChecker(OrderDTO orderDTO, List<Sku> serverSkuLis,
                        CouponChecker couponChecker,Integer maxSkuLimit){
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuLis;
        this.couponChecker = couponChecker;
        this.maxSkuLimit = maxSkuLimit;

    }

    public void isOk(){
        BigDecimal serveTotalPrice = new BigDecimal("0");
        List<SkuInfoDTO> skuInfoDTOList = new ArrayList<>();

        this.skuNotOnSale(orderDTO.getSkuInfoList().size(),this.serverSkuList.size());

        for (int i = 0;i<this.serverSkuList.size();i++){
            Sku sku = this.serverSkuList.get(i);
            SkuInfoDTO skuInfoDTO = this.orderDTO.getSkuInfoList().get(i);
            this.containsSoldOutSku(sku);
            this.beyondSkuStock(sku,skuInfoDTO);
            this.beyondMxSkuLimit(skuInfoDTO);
        }
    }

    private void skuNotOnSale(int count1,int count2){
        if(count1!=count2){
            throw new ParameterException(50002);
        }
    }

    private void containsSoldOutSku(Sku sku){
        if(sku.getStock() == 0){
            throw new ParameterException(50001);
        }
    }

    private void beyondSkuStock(Sku sku,SkuInfoDTO skuInfoDTO){
        if(sku.getStock()<skuInfoDTO.getCount()){
            throw  new ParameterException(50003);
        }
    }

    private void beyondMxSkuLimit(SkuInfoDTO skuInfoDTO){
        if(skuInfoDTO.getCount()>this.maxSkuLimit){
            throw new ParameterException(50004);
        }
    }
}
