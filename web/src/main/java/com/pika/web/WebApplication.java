package com.pika.web;

import com.pika.common.utils.Bootstrap;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.pika.web.mapper")
@SpringBootApplication
@ComponentScan(basePackages = {"com.pika.web","com.pika.common"})
@EnableDiscoveryClient
public class WebApplication {
    public static void main(String[] args) {
        Bootstrap.PikaSpringBootApplication(WebApplication.class, args);
    }
}
