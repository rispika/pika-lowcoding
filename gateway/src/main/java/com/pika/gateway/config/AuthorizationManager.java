package com.pika.gateway.config;

import com.pika.common.constant.RedisConstant;
import com.pika.gateway.secutity.SecurityUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 授权管理器(鉴权管理类)
 * 返回new AuthorizationDecision(true)代表授予权限访问服务，为false则是拒绝。
 *
 * @author RIS
 * @date 2023/07/15
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication.map(auth -> {
            SecurityUserDetails userSecurity = (SecurityUserDetails) auth.getPrincipal();
            String path = authorizationContext.getExchange().getRequest().getURI().getPath();

            for (GrantedAuthority authority : auth.getAuthorities()) {
                Set<Object> paths = redisTemplate.opsForSet().members(RedisConstant.PERMISSION_PREFIX + authority.getAuthority());
                //对客户端访问路径与用户角色进行匹配
                for (Object p : paths) {
                    if (Pattern.matches((String) p, path)) {
                        return new AuthorizationDecision(true);
                    }
                }
            }
            return new AuthorizationDecision(false);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }
}