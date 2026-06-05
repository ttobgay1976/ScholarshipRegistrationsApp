package com.sprms.registration.api.services;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmDTO.StudentApiResponseDTO;
import com.sprms.registration.frmDTO.TokenResponseDTO;

import io.github.resilience4j.retry.annotation.Retry;

@Service
public class BcseaAuthNmarksService {

	// logger
	private static final Logger logger = LoggerFactory.getLogger(BcseaAuthNmarksService.class);

	// private final declaration
	private final RestTemplate _restTemplate;
	private final APISchemaConfig _apiSchemaConfig;
	private final StudentMapperService _studentMapperService;

	private TokenResponseDTO cachedToken;
	private long expiryTime;

	// constructor
	public BcseaAuthNmarksService(RestTemplateBuilder builder, APISchemaConfig apiSchemaConfig,
			StudentMapperService studentMapperService) {
		this._restTemplate = builder.build();
		this._apiSchemaConfig = apiSchemaConfig;
		this._studentMapperService = studentMapperService;
	}

	// get token from BCSEA Auth2 url
	public synchronized String getAccessToken() {

		logger.info("@@@Calling the getAccessToken proc..................");

		try {

			// reuse token if valid
			if (cachedToken != null && System.currentTimeMillis() < expiryTime) {
				return cachedToken.getAccess_token();
			}

			// String url = authBaseUrl + "/authentication/v1/authenticate";
			String url = _apiSchemaConfig.getBcseaTokenUrl();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "client_credentials");
			body.add("client_id", _apiSchemaConfig.getBcseaClientKey());
			body.add("client_secret", _apiSchemaConfig.getBcseaSecretKey());

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

			ResponseEntity<TokenResponseDTO> response = _restTemplate.exchange(url, HttpMethod.POST, request,
					TokenResponseDTO.class);

			cachedToken = response.getBody();

			// 🔥 safety check
			if (cachedToken == null || cachedToken.getAccess_token() == null) {
				throw new RuntimeException("Failed to fetch access token from NDI");
			}

			Integer expiresIn = cachedToken.getExpires_in();
			if (expiresIn == null || expiresIn <= 0) {
				expiresIn = 3600; // fallback 1 hour
			}

			// 🔥 safe expiry calculation
			expiryTime = System.currentTimeMillis() + ((expiresIn - 30L) * 1000L);

			return cachedToken.getAccess_token();

		} catch (HttpClientErrorException e) {

			logger.error("Error while fetching access token", e);
			throw new RuntimeException("Unable to fetch BCSEA access token", e);

		} catch (Exception e) {
			logger.error("Error while fetching access token", e);
			throw new RuntimeException("Unable to fetch BCSEA access token", e);
		}
	}

	
	// GET THE STUDENT MARKS FROM THIS RESTTEMPATE
	// MODIFIED ON DT 01/05/2026
	@Retry(name = "bcseaApi", fallbackMethod = "bcseaFallback")
	public StudentApiResponseDTO getStudentMarks(String indexNo, String token) {

		logger.info("@@@Calling getStudentMarks for indexNo: {}", indexNo);

		String url = _apiSchemaConfig.getBcseaApiUrl().replace("{indexNo}", indexNo);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<Void> request = new HttpEntity<>(headers);

		try {

			ResponseEntity<StudentApiResponseDTO> response = _restTemplate.exchange(url, HttpMethod.GET, request,
					StudentApiResponseDTO.class);

			if (response == null || response.getBody() == null) {
				throw new RuntimeException("Null response from BCSEA API");
			}

			StudentApiResponseDTO body = response.getBody();

			logger.info("BCSEA Response received successfully");

			if (body.getStudents() == null || body.getStudents().getStudent() == null
					|| body.getStudents().getStudent().isEmpty()) {
				throw new RuntimeException("Empty BCSEA response");
			}

			return body;

		} catch (HttpClientErrorException e) {

			logger.error("BCSEA CLIENT ERROR: {}", e.getResponseBodyAsString());
			throw e;

		} catch (HttpServerErrorException e) {

			logger.error("BCSEA SERVER ERROR", e);
			throw e;

		} catch (Exception e) {

			logger.error("BCSEA UNKNOWN ERROR", e);
			throw e;
		}
	}

	// THIS IS THE FALLBACK METHOD
	public StudentApiResponseDTO bcseaFallback(String indexNo, String token, Throwable ex) {

		System.out.println("❌ BCSEA API FAILED after retries for indexNo: " + indexNo);
		System.out.println("ERROR: " + ex.getMessage());

		throw new RuntimeException("BCSEA service unavailable. Please try again later.");
	}
}
