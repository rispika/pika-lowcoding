package com.pika.gateway.secutity;

import cn.hutool.core.date.DateField;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pika.common.R;
import com.pika.common.constant.RedisConstant;
import com.pika.common.utils.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 身份验证成功处理程序
 *
 * @author RIS
 * @date 2023/07/15
 */
@Component
@Slf4j
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {
//    @Value("${login.timeout}")
//    private int timeout=3600;//默认一小时
    private final int rememberMe=180;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @SneakyThrows
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        ObjectMapper mapper = new ObjectMapper();
        //设置body
        R r = null;
        String remember_me=exchange.getRequest().getHeaders().getFirst("Remember-me");

        List<? extends GrantedAuthority> list= new ArrayList<>(authentication.getAuthorities());
        try {
            Map<String, Object> load = new HashMap<>();
            load.put("username",authentication.getName());
            load.put("role",list.isEmpty()?null:list.get(0).getAuthority());//这里只添加了一种角色 实际上用户可以有不同的角色类型
            String token;
            log.info(authentication.toString());
            if (remember_me==null) {
//                token=JWTUtils.creatToken(load,3600*24);
                token = JwtUtil.creatToken(load, DateField.HOUR, 24);
                response.addCookie(ResponseCookie.from("token", token).path("/").build());
                //maxAge默认-1 浏览器关闭cookie失效
                redisTemplate.opsForValue().set(RedisConstant.LOGIN_TOKEN_CACHE_PREFIX + authentication.getName(), token, 1, TimeUnit.DAYS);
            }else {
                token = JwtUtil.creatToken(load, DateField.HOUR, 24 * 180);
                response.addCookie(ResponseCookie.from("token", token).maxAge(Duration.ofDays(rememberMe)).path("/").build());
                redisTemplate.opsForValue().set(RedisConstant.LOGIN_TOKEN_CACHE_PREFIX + authentication.getName(), token, rememberMe, TimeUnit.SECONDS);//保存180天
            }

            r = R.ok(200, "登录成功!").data("token", token);
        } catch (Exception ex) {
            ex.printStackTrace();
            r = R.fail(405, "登录失败!");
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(mapper.writeValueAsBytes(r));
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

}