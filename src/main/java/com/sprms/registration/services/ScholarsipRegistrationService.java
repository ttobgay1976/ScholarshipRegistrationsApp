package com.sprms.registration.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sprms.registration.DTOMapper.ScholarshipRegistrationDTOMapper;
import com.sprms.registration.api.repository.StudentProfileRepository;
import com.sprms.registration.api.services.BcseaAuthNmarksService;
import com.sprms.registration.api.services.StudentMapperService;
import com.sprms.registration.api.services.StudentServices;
import com.sprms.registration.api.services.ValidateDuplicateStudentIndexNumber;
import com.sprms.registration.applicationEnums.ApplicationStatus;
import com.sprms.registration.dao.ScholarsipRegistrationRepository;
import com.sprms.registration.frmDTO.ScholarshipRegistrationDTO;
import com.sprms.registration.frmDTO.StudentApiResponseDTO;
import com.sprms.registration.frmDTO.StudentDTO;
import com.sprms.registration.frmDTO.StudentProfileDTO;
import com.sprms.registration.hbmbean.ScholarshipRegistration;
import com.sprms.registration.hbmbean.SupportingFiles;
import com.sprms.registration.mailServices.EmailServices;
import com.sprms.registration.utils.DateUtil;

@Service
public class ScholarsipRegistrationService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(ScholarsipRegistrationService.class);

	// call the repository
	private final ScholarsipRegistrationRepository _scholarsipRegistrationRepository;
	private final SupportingFilesService _supportingFilesService;
	private final EmailServices _emailServices;
	private final BcseaAuthNmarksService _bcseaApiTokenService;
	private final StudentServices _studentServices;
	private final StudentMapperService _studentMapperService;
	private final ValidateDuplicateStudentIndexNumber _validateDuplicateStudentIndexNumber;

	// constructor
	public ScholarsipRegistrationService(ScholarsipRegistrationRepository scholarsipRegistrationRepository,
			SupportingFilesService supportingFilesService, EmailServices emailServices,
			BcseaAuthNmarksService bcseaApiTokenService, StudentServices studentServices,
			StudentMapperService studentMapperService,
			ValidateDuplicateStudentIndexNumber validateDuplicateStudentIndexNumber) {
		this._scholarsipRegistrationRepository = scholarsipRegistrationRepository;
		this._supportingFilesService = supportingFilesService;
		this._emailServices = emailServices;
		this._bcseaApiTokenService = bcseaApiTokenService;
		this._studentServices = studentServices;
		this._studentMapperService = studentMapperService;
		this._validateDuplicateStudentIndexNumber = validateDuplicateStudentIndexNumber;
	}

	// file saving path and directories
	@Value("${file.upload-dir}")
	private String uploadDir;

	// MAIN NAVEGATION POIN FOR CALLING THE SAVE LOGIV
	public ScholarshipRegistrationDTO saveScholarshipDetails(ScholarshipRegistrationDTO dto) throws Exception {

		logger.info("@@@START: saveScholarshipDetails");

		if (dto == null) {
			throw new RuntimeException("Invalid request");
		}

		String country = dto.getCountryOfCompletion();

		if (country == null || country.isBlank()) {
			throw new RuntimeException("Country of completion is required");
		}

		ScholarshipRegistration savedRegistration;

		// FOREIGN APPLICANT FLOW
		if ("Other".equalsIgnoreCase(country)) {

			savedRegistration = saveRegistrationForCountryOther(dto);

		}

		// BHUTAN APPLICANT FLOW
		else if ("Bhutan".equalsIgnoreCase(country)) {

			savedRegistration = saveRegistrationForCountryBhutan(dto);

		}
		// INVALID COUNTRY

		else {

			throw new RuntimeException("Invalid country of completion");
		}

		logger.info("@@@END: saveScholarshipDetails");

		return ScholarshipRegistrationDTOMapper.toDTO(savedRegistration);
	}

	/*-------THIS WORKING PROCEDURE--------*/
	// THIS PROCEDURE WILL SAVE THE PARENT TABLE (ScholarshipRegistration table)
	private ScholarshipRegistration saveScholarshipRegistration(ScholarshipRegistrationDTO dto) {

		ScholarshipRegistration entity;

		// UPDATE
		if (dto.getId() != null) {

			entity = _scholarsipRegistrationRepository.findById(dto.getId())
					.orElseThrow(() -> new RuntimeException("Record not found"));

			ScholarshipRegistrationDTOMapper.updateEntityFromDTO(dto, entity);
			entity.setUpdatedAt(DateUtil.getCurrentDateTime());

		} else {

			// NEW SAVE
			entity = ScholarshipRegistrationDTOMapper.toEntity(dto);
			entity.setId(Long.parseLong(DateUtil.getUniqueID()));
			entity.setCreatedAt(DateUtil.getCurrentDateTime());
		}

		// COMMON FIELDS
		entity.setStatus(ApplicationStatus.SUBMITTED);

		// DOB CONVERSION (COMMON FOR BOTH)
		if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().isBlank()) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			LocalDate dob = LocalDate.parse(dto.getDateOfBirth(), formatter);
			entity.setDateOfBirth(dob);
		}

		return _scholarsipRegistrationRepository.save(entity);
	}

	/*--------------------------------------------------------------------------*/
	// THIS METHOD IS USE ONLY FOR THE STUDENT WHO'S COUNTRY OF COMPLETION IS OTHER
	// CREATED ON 12/05/2026

	public ScholarshipRegistration saveRegistrationForCountryOther(ScholarshipRegistrationDTO dto) throws Exception {

		logger.info("@@@START: saveRegistrationForCountryOther");

		if (dto == null) {
			throw new RuntimeException("Invalid request");
		}

		// Validate file upload
		if (dto.getFiles() == null || dto.getFiles().isEmpty()) {

			throw new IllegalArgumentException("File upload required for foreign applicant");
		}

		// SAVE REGISTRATION
		ScholarshipRegistration savedRegistration = saveScholarshipRegistration(dto);

		// SAVE FILES
		_supportingFilesService.saveSupportingFiles(savedRegistration, dto.getFiles());

		logger.info("@@@END: saveRegistrationForCountryOther");

		return savedRegistration;
	}

	// THIS METHOD IS USE ONLY FOR THE STUDENT WHO'S COUNTRY OF COMPLETION IS BHUTAN
	// CREATED ON 12/05/2026
	public ScholarshipRegistration saveRegistrationForCountryBhutan_OLD(ScholarshipRegistrationDTO dto) throws Exception {

		logger.info("@@@START: saveRegistrationForCountryBhutan");

		if (dto == null) {
			throw new RuntimeException("Invalid request");
		}

		// VALIDATE INDEX NUMBER
		if (dto.getIndexNumber() == null || dto.getIndexNumber().isBlank()) {

			throw new RuntimeException("Index number is required for BCSEA verification");
		}

		// CHECK FOR THE DUPLICATE STUDENT INDEXNUMBER
		_validateDuplicateStudentIndexNumber.validateDuplicateIndex(dto.getIndexNumber());

		// GET BCSEA TOKEN
		String bcseaToken = _bcseaApiTokenService.getAccessToken();

		// FETCH BCSEA DATA
		StudentApiResponseDTO bcseaResponse = _bcseaApiTokenService.getStudentMarks(dto.getIndexNumber(), bcseaToken);

		// VALIDATE BCSEA RESPONSE
		if (bcseaResponse == null || bcseaResponse.getStudents() == null
				|| bcseaResponse.getStudents().getStudent() == null
				|| bcseaResponse.getStudents().getStudent().isEmpty()) {

			logger.warn("@@@BCSEA returned no data - aborting registration");

			throw new RuntimeException("BCSEA marks not available. Registration aborted.");
		}

		// SAVE REGISTRATION
		ScholarshipRegistration savedRegistration = saveScholarshipRegistration(dto);

		// TRANSFORM STUDENT DATA
		List<StudentProfileDTO> students = _studentMapperService.transform(bcseaResponse);

		// SAVE STUDENT PROFILE
		_studentServices.saveStudentProfiles(students, savedRegistration);

		logger.info("@@@END: saveRegistrationForCountryBhutan");

		return savedRegistration;
	}

	// NEW PROCEDURE TO SAVE STUDENT WITH FILE
	public ScholarshipRegistration saveRegistrationForCountryBhutan(ScholarshipRegistrationDTO dto) throws Exception {

		logger.info("@@@START: saveRegistrationForCountryBhutan");

		if (dto == null) {
			throw new RuntimeException("Invalid request");
		}

		// VALIDATE INDEX NUMBER
		if (dto.getIndexNumber() == null || dto.getIndexNumber().isBlank()) {
			throw new RuntimeException("Index number is required for BCSEA verification");
		}

		// CHECK DUPLICATE
		_validateDuplicateStudentIndexNumber.validateDuplicateIndex(dto.getIndexNumber());

		// GET BCSEA DATA
		String token = _bcseaApiTokenService.getAccessToken();

		StudentApiResponseDTO bcseaResponse = _bcseaApiTokenService.getStudentMarks(dto.getIndexNumber(), token);

		// VALIDATE RESPONSE
		if (bcseaResponse == null || bcseaResponse.getStudents() == null
				|| bcseaResponse.getStudents().getStudent() == null
				|| bcseaResponse.getStudents().getStudent().isEmpty()) {

			logger.warn("@@@BCSEA returned no data - aborting registration");
			throw new RuntimeException("BCSEA marks not available. Registration aborted.");
		}

		// TRANSFORM STUDENT DATA
		List<StudentProfileDTO> students = _studentMapperService.transform(bcseaResponse);

		// CHECK REPEATER FLAG (IMPORTANT)
		boolean isRepeater = students.stream().anyMatch(s -> "REPEATER".equalsIgnoreCase(s.getType()));

		// SAVE REGISTRATION
		ScholarshipRegistration savedRegistration = saveScholarshipRegistration(dto);

		// SAVE STUDENT PROFILE
		_studentServices.saveStudentProfiles(students, savedRegistration);

		// 🔥 FILE SAVE LOGIC (ONLY FOR REPEATER)
		if (isRepeater) {

			logger.info("@@@Repeater detected - saving supporting document");

			if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {

				// SAVE FILES
				_supportingFilesService.saveSupportingFiles(savedRegistration, dto.getFiles());

			} else {
				logger.warn("@@@Repeater student but no document uploaded");
				throw new RuntimeException("Repeater document is required");
			}

		} else {
			logger.info("@@@Regular student - skipping file upload");
		}

		logger.info("@@@END: saveRegistrationForCountryBhutan");

		return savedRegistration;
	}

}
