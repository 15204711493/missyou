package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.dto.OrderAddressDTO;
import com.lin.missyou.model.BaseEntity.BaseEntity;
import com.lin.missyou.until.CommonUntil;
import com.lin.missyou.until.GenericAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "delete_time is null")
@Table(name = "`Order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalPrice;
    private Long totalCount;
    private String snapImg;
    private String snapTitle;
    private Date expiredTime;
    private Date placedTime;

    private String snapItems;

    private String snapAddress;

    private String prepayId;
    private BigDecimal finalTotalPrice;
    private Integer status;


    @JsonIgnore
    public OrderStatus getStatusEnum(){
        return OrderStatus.toType(this.status);
    }

    public Boolean needCancel(){
        if(!this.getStatusEnum().equals(OrderStatus.UNPAID)){
            return  true;
        }
        Boolean outDate = CommonUntil.isOutDate(this.getExpiredTime());
        if(outDate){
            return true;
        }
        return false;
    }



    public void setSnapItems(List<OrderSku> orderSkuList){
        if(orderSkuList.isEmpty()){
            return;
        }
        this.snapItems = GenericAndJson.objectToJson(orderSkuList);
    }

    public List<OrderSku> getSnapItems(){
        List<OrderSku> orderSkus = GenericAndJson.jsonToObject(this.snapItems,
                new TypeReference<List<OrderSku>>() {
                });
        return orderSkus;
    }

    public OrderAddressDTO getSnapAddress(){
        if(this.snapAddress == null){
            return null;
        }
        OrderAddressDTO o = GenericAndJson.jsonToObject(this.snapAddress,
                new TypeReference<OrderAddressDTO>() {
                });
        return  o;
    }

    public void setSnapAddress(OrderAddressDTO address){
        this.snapAddress = GenericAndJson.objectToJson(address);
    }
}
