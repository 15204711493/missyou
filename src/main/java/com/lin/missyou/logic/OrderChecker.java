package com.lin.missyou.logic;


import com.lin.missyou.bo.SkuOrderBO;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.OrderSku;
import com.lin.missyou.model.Sku;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderChecker {

    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponChecker couponChecker;
    private Integer maxSkuLimit;

    @Getter
    private List<OrderSku> orderSkuList  = new ArrayList<>();

    public OrderChecker(OrderDTO orderDTO, List<Sku> serverSkuLis,
                        CouponChecker couponChecker, Integer maxSkuLimit) {
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuLis;
        this.couponChecker = couponChecker;
        this.maxSkuLimit = maxSkuLimit;

    }

    public String getLeaderImg(){
        return this.serverSkuList.get(0).getImg();
    }

    public String getLeaderTitle(){
        return this.serverSkuList.get(0).getTitle();
    }

    public Integer getTotalCount(){
        return this.orderDTO.getSkuInfoList().stream()
                .map(SkuInfoDTO::getCount)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public void isOk() {
        BigDecimal serveTotalPrice = new BigDecimal("0");
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        this.skuNotOnSale(orderDTO.getSkuInfoList().size(), this.serverSkuList.size());

        for (int i = 0; i < this.serverSkuList.size(); i++) {
            Sku sku = this.serverSkuList.get(i);
            SkuInfoDTO skuInfoDTO = this.orderDTO.getSkuInfoList().get(i);
            this.containsSoldOutSku(sku);
            this.beyondSkuStock(sku, skuInfoDTO);
            this.beyondMxSkuLimit(skuInfoDTO);

            serveTotalPrice.add(this.calculateSkuOrderPrice(sku, skuInfoDTO));
            skuOrderBOList.add(new SkuOrderBO(sku, skuInfoDTO));
            this.orderSkuList.add(new OrderSku(skuInfoDTO,sku));
        }
        this.totalPriceIOk(orderDTO.getTotalPrice(), serveTotalPrice);
        if(couponChecker!=null){
            this.couponChecker.isOk();
            this.couponChecker.canBeUsed(skuOrderBOList,serveTotalPrice);
            this.couponChecker.finalTotalPriceIsOk(orderDTO.getFinalTotalPrice(),serveTotalPrice);
        }
    }


    private void totalPriceIOk(BigDecimal orderTotalPrice, BigDecimal serverTotalPrice) {
        if (orderTotalPrice.compareTo(serverTotalPrice) != 0) {
            throw new ParameterException(50005);
        }
    }


    private BigDecimal calculateSkuOrderPrice(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() <= 0) {
            throw new ParameterException(50007);
        }
        return sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
    }


    private void skuNotOnSale(int count1, int count2) {
        if (count1 != count2) {
            throw new ParameterException(50002);
        }
    }

    private void containsSoldOutSku(Sku sku) {
        if (sku.getStock() == 0) {
            throw new ParameterException(50001);
        }
    }

    private void beyondSkuStock(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (sku.getStock() < skuInfoDTO.getCount()) {
            throw new ParameterException(50003);
        }
    }

    private void beyondMxSkuLimit(SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() > this.maxSkuLimit) {
            throw new ParameterException(50004);
        }
    }
}
