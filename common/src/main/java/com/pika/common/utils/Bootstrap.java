package com.pika.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class Bootstrap {

    public static void PikaSpringBootApplication(Class<?> aClass, String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(aClass, args);
        ConfigurableEnvironment env = context.getEnvironment();
        String port = env.getProperty("server.port");
        log.info("\n=======================================================================\n" +
                        "\t Local: \t http://localhost:{}\n" +
                        "\t Swagger: \t http://localhost:{}/swagger-ui.html" +
                        "\n=======================================================================\n",
                port,
                port);
    }

}
