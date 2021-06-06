package com.lin.missyou.vo;


import com.lin.missyou.model.Activity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class ActivityPureVo {

    private Long id;
    private String title;
    private String entranceImg;
    private Boolean online;
    private String remark;
    private String startTime;
    private String endTime;

    public  ActivityPureVo(Activity activity){
        BeanUtils.copyProperties(activity,this);
    }

//    public ActivityPureVO(Object object){
//        BeanUtils.copyProperties(object, this);
//    }

}
