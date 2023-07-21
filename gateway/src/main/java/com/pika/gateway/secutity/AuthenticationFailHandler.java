package com.pika.gateway.secutity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pika.common.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * 身份验证失败处理程序
 *
 * @author RIS
 * @date 2023/07/15
 */
@Slf4j
@Component
public class AuthenticationFailHandler  implements ServerAuthenticationFailureHandler {

    @SneakyThrows
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
//        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");
        R r = R.fail(400, e.getMessage());
        log.error("access forbidden path={}", webFilterExchange.getExchange().getRequest().getPath());
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(r));
        return response.writeWith(Mono.just(dataBuffer));
    }
}