package com.pika.gateway.secutity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pika.common.R;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {
    @SneakyThrows
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        HashMap<String, String> map = new HashMap<>();
        //删除token
        response.addCookie(ResponseCookie.from("token", "logout").maxAge(0).path("/").build());
        R r = R.fail(220, "退出成功");
        ObjectMapper mapper = new ObjectMapper();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(mapper.writeValueAsBytes(r));
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}