package com.sprms.registration.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.sprms.registration.api.services.NdiAuthServices;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

	// 🔹 PUBLIC CLIENT (NO TOKEN, ONLY FOR AUTH API)
	@Bean(name = "publicWebClient")
	public WebClient publicWebClient() {
		return WebClient.builder().build();
	}

	// 🔹 SECURE CLIENT (WITH TOKEN INTERCEPTOR)
	@Bean(name = "secureWebClient")
	public WebClient secureWebClient(ObjectProvider<NdiAuthServices> tokenProvider) {

		return WebClient.builder().filter((request, next) -> {

			NdiAuthServices tokenService = tokenProvider.getIfAvailable();

			String token = tokenService.getAccessToken();

			System.out.println("🔥 TOKEN ATTACHED");

			ClientRequest updated = ClientRequest.from(request).header("Authorization", "Bearer " + token).build();

			return next.exchange(updated);
		}).build();

	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(request -> {
			System.out.println("Request: " + request.method() + " " + request.url());
			return Mono.just(request);
		});
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(response -> {
			System.out.println("Response Status: " + response.statusCode());
			return Mono.just(response);
		});
	}

}
