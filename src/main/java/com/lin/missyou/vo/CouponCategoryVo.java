package com.lin.missyou.vo;

import com.lin.missyou.model.Category;
import com.lin.missyou.model.Coupon;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CouponCategoryVo extends CouponPureVo{
    private List<CategoryPureVo> categoryPureVoList = new ArrayList<>();

    public CouponCategoryVo(Coupon coupon) {
        super(coupon);
        List<Category> categoryList = coupon.getCategoryList();
        categoryList.forEach(category -> {
            CategoryPureVo categoryPureVo = new CategoryPureVo(category);
            categoryPureVoList.add(categoryPureVo);
        });
    }
}
