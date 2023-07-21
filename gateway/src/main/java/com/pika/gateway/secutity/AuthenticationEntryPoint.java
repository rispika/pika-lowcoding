package com.pika.gateway.secutity;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pika.common.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 接口认证入口类
 * 如果客户端没有认证授权就直接访问服务接口，然后就会调用这个类，返回的状态码是401
 *
 * @author RIS
 * @date 2023/07/15
 */
@Slf4j
@Component
public class AuthenticationEntryPoint extends HttpBasicServerAuthenticationEntryPoint {
    @SneakyThrows
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");
        R r = R.fail(401, "请先进行登录!");
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(r));
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
