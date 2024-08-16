package com.accounting_manager.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("accounting-engine",r -> r.path("/api/v1/invoice-engine")
						.uri("http://localhost:8081"))
				.route("accounting-auth",r -> r.path("/api/v1/auth")
						.uri("http://localhost:8082"))
				.route("accounting-management",r -> r.path("/api/v1/management")
						.uri("http://localhost:8083"))
				.route("bank-statement-engine",r -> r.path("/api/v1/bank-statement-engine")
						.uri("http://localhost:8086"))
				.build();
	}

}
