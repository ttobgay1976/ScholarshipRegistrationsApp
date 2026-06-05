package com.sprms.registration.api.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.registration.api.client.BcseaApiClient;
import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.exception.BcseaApiException;
import com.sprms.registration.exception.BcseaValidationException;
import com.sprms.registration.frmDTO.StudentApiResponseDTO;
import com.sprms.registration.frmDTO.StudentDTO;
import com.sprms.registration.frmDTO.StudentProfileDTO;
import com.sprms.registration.services.ScholarsipRegistrationService;

import io.github.resilience4j.retry.annotation.Retry;

@Service
public class BcseaStudentService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(BcseaStudentService.class);

	private final BcseaTokenService tokenService;
	private final BcseaApiClient apiClient;
	private final APISchemaConfig apiSchemaConfig;

	public BcseaStudentService(BcseaTokenService tokenService, BcseaApiClient apiClient,
			StudentMapperService mapperService, APISchemaConfig apiSchemaConfig) {

		this.tokenService = tokenService;
		this.apiClient = apiClient;
		this.apiSchemaConfig = apiSchemaConfig;
	}

	@Retry(name = "bcseaApi", fallbackMethod = "fallbackStudentMarks")
	public boolean isRepeaterStudent(String indexNo) {

		validateIndex(indexNo);

		String token = tokenService.getAccessToken();

		String url = apiSchemaConfig.getBcseaApiUrl().replace("{indexNo}", indexNo);

		StudentApiResponseDTO response = apiClient.fetchStudentDetail(url, token);

		validateResponse(response);

		List<StudentDTO> students = response.getStudents().getStudent();
		
		for (StudentDTO std : students) {
			System.out.println("@@@Checking the Student Name :"+ std.getStudentName());
			System.out.println("@@@Checking the status type:"+ std.getType());
		}

		return students.stream().anyMatch(s -> "REPEATER".equalsIgnoreCase(s.getType()));
	}

	public boolean fallbackStudentMarks(String indexNo, Throwable ex) {

		logger.error("BCSEA API failed after retries for indexNo: {}", indexNo, ex);

		throw new BcseaApiException("BCSEA service unavailable. Please try again later.", ex);
	}

	// ================= VALIDATION HELPERS =================

	private void validateIndex(String indexNo) {
		if (indexNo == null || indexNo.isBlank()) {
			throw new BcseaValidationException("Index number is required");
		}
	}

	private void validateResponse(StudentApiResponseDTO response) {
		if (response == null || response.getStudents() == null || response.getStudents().getStudent() == null
				|| response.getStudents().getStudent().isEmpty()) {
			throw new BcseaApiException("Empty BCSEA response");
		}
	}

	private void validateStudents(List<StudentProfileDTO> students) {
		if (students == null || students.isEmpty()) {
			throw new BcseaApiException("No student data found");
		}
	}

}