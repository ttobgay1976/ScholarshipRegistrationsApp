package com.sprms.registration.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		return builder.additionalInterceptors((request, body, execution) -> {

			System.out.println("➡️ Request URI: " + request.getURI());
			System.out.println("➡️ Headers: " + request.getHeaders());

			ClientHttpResponse response = execution.execute(request, body);

			System.out.println("⬅️ Status: " + response.getStatusCode());

			return response;
		})

				.build();
	}
}
