package com.lin.missyou.core.money;

import java.math.BigDecimal;

public interface IMoneyDiscount {
    BigDecimal discount(BigDecimal origin,BigDecimal discount);
}
