package com.lin.missyou.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.User;
import com.lin.missyou.repository.UserRepository;
import com.lin.missyou.until.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Value("${wx.code2session}")
    private String code2SessionUrl;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appsecret}")
    private String appsecret;


    public String code2Session(String code) {
        String url = MessageFormat.format(this.code2SessionUrl, this.appid, this.appsecret, code);
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        String sessionText = restTemplate.getForObject(url, String.class);
        Map<String, Object> session = new HashMap<>();
        try {
             session = mapper.readValue(sessionText, Map.class);
            System.out.println(session);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.registerUser(session);
    }


    private String registerUser(Map<String, Object> session) {

        String openid = (String) session.get("openid");
        if (openid == null) {
            throw new ParameterException(20004);
        }
        Optional<User> user = userRepository.findByOpenid(openid);
        if(user.isPresent()){
            return JwtToken.makeToken(user.get().getId());
        }
        User userBuild = User.builder().openid(openid).build();
        userRepository.save(userBuild);
        Long uid = userBuild.getId();
        return JwtToken.makeToken(uid);

    }
}
