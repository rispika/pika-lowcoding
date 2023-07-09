package com.pika.gateway;

import com.pika.common.utils.Bootstrap;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		Bootstrap.PikaSpringBootApplication(GatewayApplication.class, args);
	}

}
