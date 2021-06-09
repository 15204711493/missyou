package com.lin.missyou.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

//@Component
public class HalfUpRound implements IMoneyDiscount{

    @Override
    public BigDecimal discount(BigDecimal origin, BigDecimal discount) {
        BigDecimal actualMoney = origin.multiply(discount);
        BigDecimal finalMoney = actualMoney.setScale(2, RoundingMode.HALF_EVEN);
        return finalMoney;
    }
}
