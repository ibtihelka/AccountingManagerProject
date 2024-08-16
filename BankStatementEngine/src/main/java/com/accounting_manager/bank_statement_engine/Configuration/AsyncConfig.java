package com.accounting_manager.bank_statement_engine.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set the core pool size as needed.
        executor.setMaxPoolSize(10); // Set the maximum pool size as needed.
        executor.setQueueCapacity(25); // Set the queue capacity as needed.
        executor.setThreadNamePrefix("Async-"); // Set a prefix for thread names.
        executor.initialize();
        return executor;
    }
}
