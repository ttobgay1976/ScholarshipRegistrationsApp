package com.sprms.registration.api.client;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sprms.registration.frmbean.StudentApiResponseDTO;

@Component
public class BcseaApiClient {

    private final RestTemplate restTemplate;

    public BcseaApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StudentApiResponseDTO fetchStudentDetail(String url, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<StudentApiResponseDTO> response =
                restTemplate.exchange(url, HttpMethod.GET, request, StudentApiResponseDTO.class);

        return response.getBody();
    }
}