package com.pika.gateway.client;

import com.pika.common.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("pika-lowcoding-system")
public interface UserClient {

    @GetMapping("/user/getByUsername/{username}")
    R getByUsername(@PathVariable("username") String username);

}
