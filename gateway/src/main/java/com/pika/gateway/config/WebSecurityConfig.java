package com.pika.gateway.config;

import com.pika.gateway.secutity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;

/**
 * 网络安全配置
 *
 * @author RIS
 * @date 2023/07/16
 */
@EnableWebFluxSecurity
@Configuration
@Slf4j
public class WebSecurityConfig {
    @Resource
    SecurityUserDetailsService securityUserDetailsService;
    @Resource
    AuthorizationManager authorizationManager;
    @Resource
    AccessDeniedHandler accessDeniedHandler;
    @Resource
    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Resource
    AuthenticationFailHandler authenticationFailHandler;
    @Resource
    SecurityRepository securityRepository;
    @Resource
    CookieToHeadersFilter cookieToHeadersFilter;
    @Resource
    LogoutSuccessHandler logoutSuccessHandler;
    @Resource
    LogoutHandler logoutHandler;
    @Resource
    @Qualifier("loginManage")
    LoginManager loginManager;

    @Resource
    AuthenticationEntryPoint authenticationEntryPoint;
    private final String[] path = {
            "/favicon.ico",
            "/book/**",
            "/user/login.html",
            "/user/__MACOSX/**",
            "/user/css/**",
            "/user/login",
            "/user/fonts/**",
            "/user/images/**"};

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.addFilterBefore(cookieToHeadersFilter, SecurityWebFiltersOrder.HTTP_HEADERS_WRITER);
        //SecurityWebFiltersOrder枚举类定义了执行次序
        http.authorizeExchange(exchange -> exchange // 请求拦截处理
                                .pathMatchers(path).permitAll()
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .anyExchange().access(authorizationManager)//权限
                        //.and().authorizeExchange().pathMatchers("/user/normal/**").hasRole("ROLE_USER")
                        //.and().authorizeExchange().pathMatchers("/user/admin/**").hasRole("ROLE_ADMIN")
                        //也可以这样写 将匹配路径和角色权限写在一起
                )
                .httpBasic()
                .and()
                .formLogin().loginPage("/user/login")//登录接口
                .authenticationManager(loginManager)
                .authenticationSuccessHandler(authenticationSuccessHandler) //认证成功
                .authenticationFailureHandler(authenticationFailHandler) //登陆验证失败
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)//基于http的接口请求鉴权失败
                .and().csrf().disable()//必须支持跨域
                .logout().logoutUrl("/user/logout")
                .logoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler)
                .and().cors().configurationSource(corsConfigurationSource());
        http.securityContextRepository(securityRepository);
        //http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());//无状态 默认情况下使用的WebSession
        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 允许所有请求方法
        corsConfig.addAllowedMethod("*");
        // 允许所有域，当请求头
        corsConfig.addAllowedOriginPattern("*");
        // 允许全部请求头
        corsConfig.addAllowedHeader("*");
        // 允许携带 Authorization 头
        corsConfig.setAllowCredentials(true);
        // 允许全部请求路径
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

//    @Bean
//    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
//        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
//        managers.add(authentication -> {
//            // 其他登陆方式
//            return Mono.empty();
//        });
//        managers.add(new UserDetailsRepositoryReactiveAuthenticationManager(securityUserDetailsService));
//        return new DelegatingReactiveAuthenticationManager(managers);
//    }

}



