package com.pika.gateway.secutity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pika.common.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 授权失败处理类
 * 当访问服务接口的用户权限不够时会调用这个类，返回HTTP状态码是403
 *
 * @author RIS
 * @date 2023/07/15
 */
@Slf4j
@Component
public class AccessDeniedHandler implements ServerAccessDeniedHandler {
    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");
        R r = R.fail(403, "未授权禁止访问");
        log.error("access forbidden path={}", exchange.getRequest().getPath());
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(r));
        return response.writeWith(Mono.just(dataBuffer));
    }
}