package com.pika.gateway.config;

import com.pika.gateway.secutity.SecurityUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Slf4j
@Component("loginManage")
public class LoginManager extends AbstractUserDetailsReactiveAuthenticationManager {

    @Resource
    @Qualifier("securityUserDetailsService")
    private SecurityUserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 进行身份验证
     *
     * @param authentication 身份验证
     * @return {@link Mono}<{@link Authentication}>
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        return retrieveUser(username)
                .filter(u -> {
                    if (u.getPassword().length() <= 8) {
                        return false;
                    }
                    String correctPwd = u.getPassword().substring(8);
                    return passwordEncoder.matches(password, correctPwd);
                })
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("账号或密码错误!"))))
                .map(u-> {
                    return new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities());
                });
    }

    /**
     * 检索用户
     *
     * @param username 用户名
     * @return {@link Mono}<{@link UserDetails}>
     */
    @Override
    protected Mono<UserDetails> retrieveUser(String username) {
        return userDetailsService.findByUsername(username);
    }
}
