package com.pika.gateway.secutity;


import com.pika.common.UserDTO.Role;
import com.pika.gateway.client.UserClientHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.pika.gateway.client.UserClientHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component("securityUserDetailsService")
@Slf4j
public class SecurityUserDetailsService implements ReactiveUserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserClientHandler UserClientHandler;

    @SneakyThrows
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        //调用数据库根据用户名获取用户
        log.info("username: {}", username);
        Map<String, Object> userMap = UserClientHandler.getUserByUsername(username).get();
        if (Objects.isNull(userMap)) {
            throw new UsernameNotFoundException("请检查用户名,不存在该用户!");
        } else {
            String encodedPassword = (String) userMap.get("password");
            List<Map<String,String>> roleList = (List<Map<String, String>>) userMap.get("roleList");
            List<SimpleGrantedAuthority> authorities = roleList.stream()
                    .map(role -> new SimpleGrantedAuthority(role.get("roleName")))
                    .collect(Collectors.toList());
            SecurityUserDetails securityUserDetails = new SecurityUserDetails(username, encodedPassword, authorities, 1L);
            return Mono.just(securityUserDetails);
        }

    }
}