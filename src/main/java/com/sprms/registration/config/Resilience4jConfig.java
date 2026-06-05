package com.sprms.registration.config;

import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class Resilience4jConfig {

	private static final Logger logger = LoggerFactory.getLogger(Resilience4jConfig.class);

	private final RetryRegistry retryRegistry;

	public Resilience4jConfig(RetryRegistry retryRegistry) {
		this.retryRegistry = retryRegistry;
	}

	@PostConstruct
	public void init() {

		retryRegistry.retry("bcseaApi").getEventPublisher().onRetry(event -> {

			logger.warn("Retry attempt: {} | Reason: {}", event.getNumberOfRetryAttempts(),
					event.getLastThrowable().getMessage());
		});

		logger.info("Retry listener registered for bcseaApi");
	}
}
