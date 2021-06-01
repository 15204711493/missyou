package com.lin.missyou.api.v1;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.model.Theme;
import com.lin.missyou.service.ThemeService;
import com.lin.missyou.vo.ThemePureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/theme")
public class ThemeController {
    @Autowired
    private ThemeService themeService;

    @GetMapping("/by/names")
    public List<ThemePureVo> getThemeGroupByNames(@RequestParam String names){

        List<String> strings = Arrays.asList(names.split(","));
        List<Theme> themeListByNames = themeService.getThemeListByNames(strings);
        List<ThemePureVo> themePureVos = new ArrayList<>();
        themeListByNames.forEach(theme->{
            Mapper mapper = DozerBeanMapperBuilder.buildDefault();
            ThemePureVo themePureVo = mapper.map(theme, ThemePureVo.class);
            themePureVos.add(themePureVo);
        });
        return themePureVos;
    }


    @GetMapping("/name/{name}/with_spu")
    public Theme getThemeByNameWithSpu(@PathVariable String name){
        Optional<Theme> themeByNameWithSpu = themeService.getThemeByNameWithSpu(name);
        return themeByNameWithSpu.orElseThrow(()->new NotFoundEcxeption(300033));
    }


}
