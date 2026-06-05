package com.sprms.registration.api.services;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmDTO.CitizenDetailDTO;
import com.sprms.registration.frmDTO.CitizenDetailsResponseDTO;
import com.sprms.registration.frmDTO.DcrcCititezenDetailsDTO;
import com.sprms.registration.frmDTO.TokenResponseDTO;

//THIS SERVICE WILL GET THE TOKEN WITH AUTH URL
//SAME TIME WILL GET TH CITIZEN DETAILS FOR THE GIVEN INDIVIDUAL
//CREATED DATE : 29/04/2026
//PLACE : YK OFFICE

@Service
public class DcrcAuthNCitizenServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(DcrcAuthNCitizenServices.class);

	// private final declaration
	private final RestTemplate _restTemplate;
	private final APISchemaConfig _apiSchemaConfig;

	private TokenResponseDTO cachedToken;
	private long expiryTime;

	// constructor
	public DcrcAuthNCitizenServices(RestTemplateBuilder builder, APISchemaConfig apiSchemaConfig) {
		this._restTemplate = builder.build();
		this._apiSchemaConfig = apiSchemaConfig;
	}

	// GET THE ACCESS TOKEN WITH PASSING THE KEYS
	public synchronized String getAccessToken() {

		logger.info("@@@Calling the getAccessToken proc..................");

		try {

			// reuse token if valid
			if (cachedToken != null && System.currentTimeMillis() < expiryTime) {
				return cachedToken.getAccess_token();
			}

			// THE TOKEN URL
			String url = _apiSchemaConfig.getDcrcTokenUrl();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "client_credentials");
			body.add("client_id", _apiSchemaConfig.getDcrcClientKey());
			body.add("client_secret", _apiSchemaConfig.getDcrcSecretKey());

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

			//System.out.println("🔥 ERROR STATUS: " + e.getStatusCode());
			//System.out.println("🔥 ERROR BODY: " + e.getResponseBodyAsString());

			logger.error("Error while fetching access token", e);
			throw new RuntimeException("Unable to fetch BCSEA access token", e);

		} catch (Exception e) {
			logger.error("Error while fetching access token", e);
			throw new RuntimeException("Unable to fetch BCSEA access token", e);
		}
	}

	// GET THE CITIZEN DETAILS BY CITIZEN ID
	public CitizenDetailDTO getCitizenDetils(String cid, String token) {

		logger.info("@@@Calling getCitizenDetils proc..................");

		try {

			String url = _apiSchemaConfig.getDcrcBaseApiUrl();

			//System.out.println("@@@URL: " + url);
			//System.out.println("@@@CID: " + cid);

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);

			ResponseEntity<DcrcCititezenDetailsDTO> response = _restTemplate.exchange(url, HttpMethod.GET, request,
					DcrcCititezenDetailsDTO.class, cid);

			DcrcCititezenDetailsDTO body = response.getBody();

			// 🔥 DEBUG LOG (safe)
			ObjectMapper mapper = new ObjectMapper();
			//System.out.println("RAW RESPONSE:");
			//System.out.println(mapper.writeValueAsString(body));

			// 🔥 SAFE NULL CHECKS
			if (body != null && body.getCitizenDetailsResponse() != null
					&& body.getCitizenDetailsResponse().getCitizenDetail() != null
					&& !body.getCitizenDetailsResponse().getCitizenDetail().isEmpty()) {

				CitizenDetailDTO apiData = body.getCitizenDetailsResponse().getCitizenDetail().get(0);

				// 🔥 MAP BEFORE RETURN (BEST PRACTICE)
				CitizenDetailDTO dto = new CitizenDetailDTO();

				dto.setCid(apiData.getCid());
				dto.setFirstName(apiData.getFirstName());
				dto.setLastName(apiData.getLastName());
				dto.setMiddleName(apiData.getMiddleName());
				dto.setDob(apiData.getDob());
				dto.setDzongkhagId(apiData.getDzongkhagId());
				dto.setDzongkhagName(apiData.getDzongkhagName());
				dto.setGewogId(apiData.getGewogId());
				dto.setGewogName(apiData.getGewogName());
				dto.setVillageName(apiData.getVillageName());
				dto.setHouseNo(apiData.getHouseNo());
				dto.setThramNo(apiData.getThramNo());
				dto.setHouseholdNo(apiData.getHouseholdNo());
				dto.setFatherName(apiData.getFatherName());
				dto.setMotherName(apiData.getMotherName());
			

				// 🔥 example mapping
				dto.setGender(mapGender(apiData.getGender()));

				return dto;
			}

			return null;

		} catch (Exception e) {
			logger.error("Error fetching citizen details", e);
			throw new RuntimeException(e);
		}

	}
	
	//HELPER CLASS TO MAP THE GENDER WHICH RETURN AS M AND F
	private String mapGender(String code) {
	    if ("M".equalsIgnoreCase(code)) return "Male";
	    if ("F".equalsIgnoreCase(code)) return "Female";
	    return "Unknown";
	}
}
