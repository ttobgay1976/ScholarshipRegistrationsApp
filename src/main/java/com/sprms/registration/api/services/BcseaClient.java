package com.sprms.registration.api.services;

import java.util.Collections;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmDTO.StudentApiResponseDTO;

@Service
public class BcseaClient {

	private final RestTemplate restTemplate;
	private final APISchemaConfig config;

	public BcseaClient(RestTemplateBuilder builder, APISchemaConfig config) {
		this.restTemplate = builder.build();
		this.config = config;
	}

	public StudentApiResponseDTO fetchStudentMarks(String indexNo, String token) {

		String url = config.getBcseaApiUrl().replace("{indexNo}", indexNo);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<Void> request = new HttpEntity<>(headers);

		ResponseEntity<StudentApiResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, request,
				StudentApiResponseDTO.class);

		return response.getBody();
	}
}