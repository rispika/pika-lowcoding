package com.pika.system;

import com.pika.common.utils.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@MapperScan("com.pika.system.mapper")
@ComponentScan(basePackages = "com.pika.common")
@ComponentScan(basePackages = "com.pika.system")
@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {
        Bootstrap.PikaSpringBootApplication(SystemApplication.class, args);
    }

}
