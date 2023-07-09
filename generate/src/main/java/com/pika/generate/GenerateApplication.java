package com.pika.generate;

import com.pika.common.utils.Bootstrap;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.pika.generate.mapper")
@ComponentScan(basePackages = {"com.pika.generate","com.pika.common"})
@SpringBootApplication
public class GenerateApplication {

    public static void main(String[] args) {
        Bootstrap.PikaSpringBootApplication(GenerateApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
