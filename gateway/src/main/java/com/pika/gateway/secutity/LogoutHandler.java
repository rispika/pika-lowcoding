package com.pika.gateway.secutity;

import com.pika.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 注销处理程序
 *
 * @author RIS
 * @date 2023/07/15
 */
@Component
@Slf4j
public class LogoutHandler implements ServerLogoutHandler {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Mono<Void> logout(WebFilterExchange webFilterExchange, Authentication authentication) {
        HttpCookie cookie=webFilterExchange.getExchange().getRequest().getCookies().getFirst("token");
        try {
            if (cookie != null) {
                Map<String,Object> userMap= JwtUtil.getTokenInfo(cookie.getValue());
                redisTemplate.delete((String) userMap.get("username"));
            }
        }catch (Exception e) {
            return Mono.error(e);
        }
        return Mono.empty();
    }
}