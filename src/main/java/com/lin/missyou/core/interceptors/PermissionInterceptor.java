package com.lin.missyou.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.UnAuthorizationException;
import com.lin.missyou.model.User;
import com.lin.missyou.repository.UserRepository;
import com.lin.missyou.service.UserService;
import com.lin.missyou.until.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    public PermissionInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<ScopeLevel> scopeLevel = this.getScopeLevel(handler);
        if (!scopeLevel.isPresent()) {
            return true;
        }
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(bearerToken)) {
            throw new UnAuthorizationException(10004);
        }
        if (!bearerToken.startsWith("Bearer")) {
            throw new UnAuthorizationException(10004);
        }
        String[] tokens = bearerToken.split(" ");
        if(!(tokens.length==2)){
            throw new UnAuthorizationException(10004);
        }
        String token =tokens[1];
        Optional<Map<String, Claim>> claims = JwtToken.getClaims(token);
        Map<String, Claim> map = claims.orElseThrow(() -> new UnAuthorizationException(10004));
        boolean valid = this.hasPermission(scopeLevel.get(), map);
        if(valid){
            this.setToThreadLocal(map);
        }

        return valid;

    }

    private void setToThreadLocal(Map<String,Claim> map){
        Long uid = map.get("uid").asLong();
        Integer scope = map.get("scope").asInt();
        User user = userService.findById(uid);
        LocalUser.set(user,scope);

    }

    private boolean hasPermission(ScopeLevel scopeLevel, Map<String, Claim> map) {
        Integer scopeLevelValue = scopeLevel.value();
        Integer scope = map.get("scope").asInt();
        if (scopeLevelValue > scope) {
            throw new ForbiddenException(100005);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalUser.clear();
        super.afterCompletion(request, response, handler, ex);
    }


    private Optional<ScopeLevel> getScopeLevel(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ScopeLevel scopeLevel = handlerMethod.getMethod().getAnnotation(ScopeLevel.class);
            if(scopeLevel == null){
                return Optional.empty();
            }
            return Optional.of(scopeLevel);
        }
        return Optional.empty();

    }

}
