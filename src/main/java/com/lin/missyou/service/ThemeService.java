package com.lin.missyou.service;

import com.lin.missyou.model.Theme;
import com.lin.missyou.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    public List<Theme> getThemeListByNames(List<String> names){
        List<Theme> themeList = themeRepository.findByNames(names);
        return  themeList;
    }

    public Optional<Theme> getThemeByNameWithSpu(String name){
        return themeRepository.findByName(name);
    }
}
