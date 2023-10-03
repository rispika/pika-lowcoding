package com.pika.gateway.secutity;

import com.pika.common.constant.RedisConstant;
import com.pika.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 用户信息上下文存储类
 *
 * @author RIS
 * @date 2023/07/15
 */
@Slf4j
@Component
public class SecurityRepository implements ServerSecurityContextRepository {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("token: {}", token);
        if (token != null) {
            try {
                Map<String,Object> userMap= JwtUtil.getTokenInfo(token);
                String result=(String)redisTemplate.opsForValue().get(RedisConstant.LOGIN_TOKEN_CACHE_PREFIX + userMap.get("username"));
                if (result==null || !result.equals(token))
                    return Mono.empty();
                SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
                Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
                log.info("权限:{}",(String) userMap.get("role"));
                authorities.add(new SimpleGrantedAuthority((String) userMap.get("role")));
                Authentication authentication=new UsernamePasswordAuthenticationToken(null, null,authorities);
                emptyContext.setAuthentication(authentication);
                return Mono.just(emptyContext);
            }catch (Exception e) {
                return Mono.empty();
            }
        }
        return Mono.empty();
    }
}