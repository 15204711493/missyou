package com.lin.missyou.until;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtToken {

    private static String jwtKey;
    private static Integer expiredTimeIn;
    private static Integer defaultScope = 8;

    @Value("${missyou.security.jwt-key}")
    public  void setJwtKey(String jwtKey) {
        JwtToken.jwtKey = jwtKey;
    }

    @Value("${missyou.security.jwt-key.token-expired-in}")
    public void setExpiredTimeIn(Integer expiredTimeIn) {
        JwtToken.expiredTimeIn = expiredTimeIn;
    }

    public static String makeToken(Long uid){
        return JwtToken.getToken(uid,JwtToken.defaultScope);
    }




    public static String makeToken(Long uid, Integer scope){
        return JwtToken.getToken(uid,scope);

    }

    public static String getToken(Long uid,Integer scope){

        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        Map<String,Date> dateMap =  JwtToken.calculateExpiredIssues();

        String jwt = JWT.create()
                .withClaim("uid",uid)
                .withClaim("scope",scope)
                .withExpiresAt(dateMap.get("expiredTime"))
                .withIssuedAt(dateMap.get("now"))
                .sign(algorithm);
        return jwt;

    }

    private static Map<String, Date> calculateExpiredIssues() {
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND,JwtToken.expiredTimeIn);
        map.put("now", now);
        map.put("expiredTime", calendar.getTime());
        return map;
    }
}
