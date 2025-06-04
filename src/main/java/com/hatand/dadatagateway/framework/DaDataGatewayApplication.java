package com.hatand.dadatagateway.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.hatand.dadatagateway.framework",
		"com.hatand.dadatagateway.controller",
		"com.hatand.dadatagateway.business",
		"com.hatand.dadatagateway.infrastructure",
		"com.hatand.dadatagateway.crosscut",
})
public class DaDataGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaDataGatewayApplication.class, args);
	}

}
