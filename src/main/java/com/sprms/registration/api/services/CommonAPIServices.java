package com.sprms.registration.api.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmbean.ApiResponseDTO;
import com.sprms.registration.frmbean.StreamDTO;

@Service
public class CommonAPIServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CommonAPIServices.class);

	// call the depency file
	private final RestTemplate _restTemplate;
	private final WebClient _secureWebClient;
	private final APISchemaConfig _apiSchemaConfig;

	// constructor ti initialize
	public CommonAPIServices(@Qualifier("secureWebClient") WebClient webClient, APISchemaConfig apiSchemaConfig,
			RestTemplateBuilder builder) {
		this._secureWebClient = webClient;
		this._apiSchemaConfig = apiSchemaConfig;
		this._restTemplate = builder.build();
	}

	// calling the external API
	public ApiResponseDTO<List<StreamDTO>> getAllStreams() {

		logger.info("@@@Calling the getAllStreams proc.................");

		try {
			List<StreamDTO> list = _secureWebClient.get().uri("http://localhost:8080/api/allstreams").retrieve()
					.bodyToFlux(StreamDTO.class).collectList().block();

			// return new ApiResponseDTO<>(true, "API is ONLINE ✅", list);
			return new ApiResponseDTO<>(true, null, list);

		} catch (Exception e) {
			logger.error("API is DOWN", e);

			return new ApiResponseDTO<>(false, "API is OFFLINE, wait for a while till API is online. ❌",
					new ArrayList<>());
		}
	}

}
