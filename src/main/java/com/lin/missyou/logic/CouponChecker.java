package com.lin.missyou.logic;

import com.lin.missyou.bo.SkuOrderBO;
import com.lin.missyou.core.enumeration.CouponType;
import com.lin.missyou.core.money.HalfEvenRound;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Category;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.until.CommonUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class CouponChecker {

    private Coupon coupon;
    private IMoneyDiscount iMoneyDiscount;

    public CouponChecker(Coupon coupon,IMoneyDiscount iMoneyDiscount) {
        this.coupon = coupon;
        this.iMoneyDiscount = iMoneyDiscount;

    }

    public void isOk() {
        Boolean isInTimeLine = CommonUntil.isInTimeLine(new Date(), coupon.getStartTime(), coupon.getEndTime());
        if (!isInTimeLine) {
            throw new ForbiddenException(40007);
        }
    }

    public void finalTotalPriceIsOk(BigDecimal orderFinalTotalPrice, BigDecimal serverTotalPrice) {

        BigDecimal serverFinalTotalPrice;

        switch (CouponType.toType(this.coupon.getType())) {
            case FULL_MINUS:
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                if(serverFinalTotalPrice.compareTo(new BigDecimal("0"))<=0){
                    throw new ForbiddenException(50008);
                }
                break;
            case FULL_OFF:
                serverFinalTotalPrice = this.iMoneyDiscount.discount(serverTotalPrice,this.coupon.getRate());
                break;
            default:
                throw new ParameterException(40009);
        }

        int compare = serverFinalTotalPrice.compareTo(orderFinalTotalPrice);
        if (compare != 0) {
            throw new ForbiddenException(50008);
        }
    }

    public void canBeUsed(List<SkuOrderBO> skuOrderBOList,BigDecimal serverTotalPrice) {
        BigDecimal orderCategoryPrice;

        if(this.coupon.getWholeStore()){
            orderCategoryPrice = serverTotalPrice;
        }else {
            List<Long> cIds = coupon.getCategoryList().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            orderCategoryPrice = this.getSumCategoryList(skuOrderBOList,cIds);
        }
        this.couponCanBeUsed(orderCategoryPrice);

    }

    private void couponCanBeUsed(BigDecimal orderCategoryPrice){
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_OFF:
            case FULL_MINUS:
                int compare = this.coupon.getFullMoney().compareTo(orderCategoryPrice);
                if(compare>0){
                    throw  new ParameterException(40008);
                }
                break;
            case NO_THRESHOLD_MINUS:
                break;
                default:
                    throw new ParameterException(40009);

        }

    }

    public BigDecimal getSumByCategory(List<SkuOrderBO> skuOrderBOList,Long cid){
        BigDecimal sum = skuOrderBOList.stream()
                .filter(sku -> sku.getCategoryId().equals(cid))
                .map(SkuOrderBO::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
        return sum;
    }

    public BigDecimal getSumCategoryList(List<SkuOrderBO> skuOrderBOList,List<Long> cIds) {
        BigDecimal sum = cIds.stream()
                .map(cId -> this.getSumByCategory(skuOrderBOList, cId))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
        return sum;

    }



//    public CouponChecker(Long id, Long uid) {
//
//    }

}
