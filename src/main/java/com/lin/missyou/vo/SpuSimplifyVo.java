package com.lin.missyou.vo;

import com.lin.missyou.model.Sku;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpuSimplifyVo {

    private Long id;
    private String title;
    private String subtitle;
    private String img;
    private String forThemeImg;
    private String price;
    private String discountPrice;
    private String description;
    private String tags;
    private Long sketchSpecId;

}
