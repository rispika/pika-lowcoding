package com.pika.gateway.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.pika.gateway.exception.NotFoundToken;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil extends JWTUtil{
    private final static String AUTH_KEY="PIKA_AUTH";
    public static String creatToken(Map<String,Object> payload, DateField dateField, int expireTime){
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(dateField, expireTime);
        Map<String,Object> body = new HashMap<String,Object>();
        //签发时间
        body.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        body.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        body.put(JWTPayload.NOT_BEFORE, now);
        //载荷
        body.putAll(payload);
        return createToken(body, AUTH_KEY.getBytes());
    }

    public static Map<String, Object> getTokenInfo(String token) {
        JWT jwt = parseToken(token);
        jwt.setKey(AUTH_KEY.getBytes());
        if (jwt.verify()) {
            JSONObject payloads = jwt.getPayloads();
            return payloads.getRaw();
        } else {
            throw new NotFoundToken("不存在token或token已过期,请重新登录");
        }
    }
}