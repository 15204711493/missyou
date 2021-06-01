package com.lin.missyou.core.configuration;

import com.lin.missyou.core.hack.AutoPrefixUrlMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class AutoPrefixConfiguration implements WebMvcRegistrations {



    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new AutoPrefixUrlMapping();
    }
}
