package com.lin.missyou.vo;

import com.lin.missyou.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CategoryPureVo {
    private Long id;
    private String name;
    private Boolean isRoot;
    private Long parentId;
    private String img;
    private Long index;

    public CategoryPureVo(Category category){
        BeanUtils.copyProperties(category,this);
    }
}
