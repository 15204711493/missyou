package com.lin.missyou.api.v1;

import com.lin.missyou.dto.TokenDTO;
import com.lin.missyou.dto.TokenGetDTO;
import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.service.WxAuthenticationService;
import com.lin.missyou.until.JwtToken;
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
        map.put("token",token);
        return map;
    }

    @PostMapping("/verify")
    public Map<String,Boolean> verify(@RequestBody TokenDTO token) {
        HashMap<String, Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verifyToken(token.getToken());
        map.put("is_valid",valid);
        return map;

    }


}
