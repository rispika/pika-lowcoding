package com.pika.gateway.secutity;

import com.pika.common.exception.NotFoundToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 将Cookie写入Http请求头中
 *
 * @author RIS
 * @date 2023/07/15
 */
@Slf4j
@Component
public class CookieToHeadersFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {

            HttpCookie cookie=exchange.getRequest().getCookies().getFirst("token");
            if (cookie != null) {
                String token = cookie.getValue();
                ServerHttpRequest request=exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION,token).build();
                return chain.filter(exchange.mutate().request(request).build());
            }
        }catch (NotFoundToken e) {
            log.error(e.getMessage());
        }

        return chain.filter(exchange);

    }

}