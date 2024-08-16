package com.accounting_manager.accounting_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@SpringBootApplication(scanBasePackages = "com.accounting_manager.accounting_management")
@EnableDiscoveryClient
public class AccountingManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountingManagementApplication.class, args);
    }
}
