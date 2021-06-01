package com.lin.missyou.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import sun.security.krb5.internal.tools.Klist;

import java.util.ArrayList;
import java.util.List;

public class PagingDozer<T,K> extends Paging {
    public PagingDozer(Page<T> pageT,Class<K> kClass){
        this.initPageParameters(pageT);
        List<T> tList = pageT.getContent();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<K> kList = new ArrayList<>();
        tList.forEach(t -> {
            K kvo = mapper.map(t, kClass);
            kList.add(kvo);
        });
        this.setItems(kList);
    }

}
