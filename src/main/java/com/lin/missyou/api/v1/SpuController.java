package com.lin.missyou.api.v1;


import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.lin.missyou.bo.PageCounter;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.model.Spu;
import com.lin.missyou.service.SpuService;
import com.lin.missyou.until.CommonUntil;
import com.lin.missyou.vo.PagingDozer;
import com.lin.missyou.vo.SpuSimplifyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ranges.Range;

import javax.persistence.Id;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {

    @Autowired
    private SpuService spuService;

    @GetMapping("/id/{id}/detail")
    public Spu getDetail(@PathVariable @Positive Long id) {
        Spu spu = spuService.getById(id);
        if (spu == null) {
            throw new NotFoundEcxeption(30003);
        }
        return spu;
    }


    @GetMapping("/id/{id}/simplify")
    public SpuSimplifyVo getSimplifySpu(@PathVariable @Positive(message = "{id.positive}") Long id){
        Spu spu = spuService.getById(id);
        SpuSimplifyVo simplifyVo = new SpuSimplifyVo();
        BeanUtils.copyProperties(spu,simplifyVo);
        return simplifyVo;

    }

    @GetMapping("/latest")
    public PagingDozer<Spu, SpuSimplifyVo> getLatestSpuList(@RequestParam(defaultValue = "0") Integer start,
                                                            @RequestParam(defaultValue = "10") Integer count) {
        PageCounter pageCounter = CommonUntil.convertToPageParameter(start,count);
        Page<Spu> page = spuService.getLatestPagingSpu(pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<Spu,SpuSimplifyVo>(page,SpuSimplifyVo.class);
    }

    @GetMapping("/by/category/{id}")
    public PagingDozer<Spu,SpuSimplifyVo> getByCategoryId(@PathVariable @Positive Long id,
                                                          @RequestParam(value = "is_root",defaultValue = "false") Boolean isRoot,
                                                          @RequestParam(defaultValue = "0") Integer start,
                                                          @RequestParam(defaultValue = "10") Integer count){

        PageCounter pageCounter = CommonUntil.convertToPageParameter(start,count);
        Page<Spu> page = spuService.getByCategory(id, isRoot, pageCounter.getPage(), pageCounter.getCount());

        return new PagingDozer<Spu,SpuSimplifyVo>(page,SpuSimplifyVo.class);

    }

}
