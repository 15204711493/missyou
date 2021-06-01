package com.lin.missyou.core.hack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;


public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {


    @Value("${missyou.api-package}")
    private String apiPackagePath ;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
       RequestMappingInfo requestMappingInfo =  super.getMappingForMethod(method, handlerType);

       if(requestMappingInfo!=null){
           String prefix = this.getPrefix(handlerType);
           RequestMappingInfo newMappingInfo =
                   RequestMappingInfo.paths(prefix).build().combine(requestMappingInfo);
           return newMappingInfo;

       }
       return requestMappingInfo;
    }

    private String getPrefix(Class<?> handlerType){
        String packageName = handlerType.getPackage().getName();
        String dotPath = packageName.replaceAll(this.apiPackagePath,"");
        return dotPath.replace(".","/");


    }
}
