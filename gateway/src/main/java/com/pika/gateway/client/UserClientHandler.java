package com.pika.gateway.client;

import com.pika.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

@Component
@Slf4j
public class UserClientHandler {

    @Lazy
    @Resource
    private UserClient userClient;

    @Async
    public Future<Map<String, Object>> getUserByUsername(String username) {
        Map<String, Object> userMap = null;
        try {
            userMap = Optional.ofNullable(userClient.getByUsername(username))
                    .filter(r -> r.getCode() >= 200 && r.getCode() < 300)
                    .map(R::getData).get();
        } catch (Exception e) {
            log.error("invoke getUserByUsername error occur. username is {}", username);
        }
        log.info("userMap: {}", userMap);
        return new AsyncResult<>(userMap);
    }

}
