package com.pika.system;

import com.pika.common.utils.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@Slf4j
@MapperScan("com.pika.system.mapper")
@ComponentScan(basePackages = "com.pika.common")
@ComponentScan(basePackages = "com.pika.system")
@SpringBootApplication
@EnableDiscoveryClient
public class SystemApplication {

    public static void main(String[] args) {
        Bootstrap.PikaSpringBootApplication(SystemApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient().newBuilder()
                .build();
    }

}
