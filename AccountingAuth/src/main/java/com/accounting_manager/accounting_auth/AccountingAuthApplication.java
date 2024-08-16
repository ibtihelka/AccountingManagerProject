package com.accounting_manager.accounting_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AccountingAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountingAuthApplication.class, args);
	}

}
