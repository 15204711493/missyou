package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lin.missyou.model.BaseEntity.BaseEntity;
import com.lin.missyou.until.GenericAndJson;
import com.lin.missyou.until.ListAndJson;
import com.lin.missyou.until.MapToJson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@Where(clause = "delete_time is null and online = 1")
public class Sku extends BaseEntity {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;

   // @Convert(converter = ListAndJson.class)
    private String specs;
    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;

    public List<Spec> getSpecs() {
        if(this.specs == null){
            return Collections.emptyList();
        }
        return GenericAndJson.jsonToObject(this.specs, new TypeReference<List<Spec>>() {
        });
    }

    public void setSpecs(String specs) {
        if(specs.isEmpty()){
            return ;
        }
        this.specs = GenericAndJson.objectToJson(specs);
    }


    public BigDecimal getActualPrice(){
        return discountPrice ==null ? this.price :this.discountPrice;
    }


    @JsonIgnore
    public List<String> getSpecValueList(){
        return this.getSpecs().stream()
                .map(Spec::getValue)
                .collect(Collectors.toList());
    }
}
