package com.lin.missyou.api.v1;

import com.lin.missyou.dto.TokenGetDTO;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.service.WxAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private WxAuthenticationService wxAuthenticationService;

    @PostMapping("")
    public Map<String,String> getToken(@RequestBody @Validated TokenGetDTO userData){
        System.out.println(userData.getType());
        Map<String,String>  map = new HashMap<>();
        String token = null;
        switch (userData.getType()){
            case USER_WX:
              token = wxAuthenticationService.code2Session(userData.getAccount());
                break;
            case USER_Email:
                break;
            default:
                throw new NotFoundEcxeption(10003);

        }

        return null;
    }
}
