package com.sprms.registration.DTOMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.sprms.registration.frmDTO.ScholarshipRegistrationDTO;
import com.sprms.registration.hbmbean.ScholarshipRegistration;

public class ScholarshipRegistrationDTOMapper {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// =========================
	// ENTITY → DTO
	// =========================
	public static ScholarshipRegistrationDTO toDTO(ScholarshipRegistration entity) {

		if (entity == null)
			return null;

		ScholarshipRegistrationDTO dto = new ScholarshipRegistrationDTO();

		dto.setId(entity.getId());
		dto.setCitizenId(entity.getCitizenId());
		dto.setFirstName(entity.getFirstName());
		dto.setMiddleName(entity.getMiddleName());
		dto.setLastName(entity.getLastName());

		dto.setDateOfBirth(entity.getDateOfBirth() != null ? entity.getDateOfBirth().format(formatter) : null);

		dto.setGender(entity.getGender());

		dto.setFatherName(entity.getFatherName());
		dto.setMotherName(entity.getMotherName());

		dto.setDzongkhagId(entity.getDzongkhagId() != null ? String.valueOf(entity.getDzongkhagId()) : null);

		dto.setDzongkhagName(entity.getDzongkhagName());

		dto.setGewogId(entity.getGewogId() != null ? String.valueOf(entity.getGewogId()) : null);

		dto.setGewogName(entity.getGewogName());
		dto.setVillageName(entity.getVillageName());

		dto.setGuardianDetail(entity.getGuardianDetail());

		dto.setIndexNumber(entity.getIndexNumber());

		dto.setEmailAddress(entity.getEmailAddress());
		dto.setContactNo(entity.getContactNo());

		dto.setCountryOfCompletion(entity.getCountryOfCompletion());
		dto.setRemarks(entity.getRemarks());

		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());

		dto.setVerified_at(entity.getVerified_at());
		dto.setVerified_by(entity.getVerified_by());
		dto.setApproved_by(entity.getApproved_by());
		dto.setApproved_at(entity.getApproved_at());

		return dto;
	}

	// =========================
	// DTO → ENTITY (SAFE FIXED)
	// =========================
	public static ScholarshipRegistration toEntity(ScholarshipRegistrationDTO dto) {

		if (dto == null)
			return null;

		ScholarshipRegistration entity = new ScholarshipRegistration();

		entity.setId(dto.getId());
		entity.setCitizenId(dto.getCitizenId());
		entity.setFirstName(dto.getFirstName());
		entity.setMiddleName(dto.getMiddleName());
		entity.setLastName(dto.getLastName());

		// =========================
		// SAFE DATE PARSING
		// =========================
		if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().isBlank()) {
			entity.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth(), formatter));
		} else {
			entity.setDateOfBirth(null);
		}

		entity.setGender(dto.getGender());

		entity.setFatherName(dto.getFatherName());
		entity.setMotherName(dto.getMotherName());

		// =========================
		// SAFE INTEGER PARSING
		// =========================
		entity.setDzongkhagId(parseInteger(dto.getDzongkhagId()));
		entity.setGewogId(parseInteger(dto.getGewogId()));

		entity.setDzongkhagName(dto.getDzongkhagName());
		entity.setGewogName(dto.getGewogName());
		entity.setVillageName(dto.getVillageName());

		entity.setGuardianDetail(dto.getGuardianDetail());

		entity.setIndexNumber(dto.getIndexNumber());

		entity.setEmailAddress(dto.getEmailAddress());
		entity.setContactNo(dto.getContactNo());

		entity.setCountryOfCompletion(dto.getCountryOfCompletion());
		entity.setRemarks(dto.getRemarks());

		entity.setCreatedAt(dto.getCreatedAt());
		entity.setUpdatedAt(dto.getUpdatedAt());

		entity.setVerified_by(dto.getVerified_by());
		entity.setVerified_at(dto.getVerified_at());
		entity.setApproved_by(dto.getApproved_by());
		entity.setApproved_at(dto.getApproved_at());

		// =========================
		// FIX STREAM MAPPING (BUG FIXED)
		// =========================
		/*
		 * if (dto.getStreamId() != null) { Stream stream = new Stream();
		 * stream.setId(dto.getStreamId()); entity.setStream(stream); }
		 */

		return entity;
	}

	// =========================
	// UPDATE ENTITY (SAFE)
	// =========================
	public static void updateEntityFromDTO(ScholarshipRegistrationDTO dto, ScholarshipRegistration entity) {

		if (dto == null || entity == null)
			return;

		entity.setCitizenId(dto.getCitizenId());
		entity.setFirstName(dto.getFirstName());
		entity.setMiddleName(dto.getMiddleName());
		entity.setLastName(dto.getLastName());

		if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().isBlank()) {
			entity.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth(), formatter));
		}

		entity.setGender(dto.getGender());

		entity.setFatherName(dto.getFatherName());
		entity.setMotherName(dto.getMotherName());

		entity.setDzongkhagId(parseInteger(dto.getDzongkhagId()));
		entity.setGewogId(parseInteger(dto.getGewogId()));

		entity.setDzongkhagName(dto.getDzongkhagName());
		entity.setGewogName(dto.getGewogName());
		entity.setVillageName(dto.getVillageName());

		entity.setGuardianDetail(dto.getGuardianDetail());

		entity.setIndexNumber(dto.getIndexNumber());

		entity.setEmailAddress(dto.getEmailAddress());
		entity.setContactNo(dto.getContactNo());

		entity.setCountryOfCompletion(dto.getCountryOfCompletion());
		entity.setRemarks(dto.getRemarks());

		entity.setUpdatedAt(dto.getUpdatedAt());

		entity.setVerified_by(dto.getVerified_by());
		entity.setVerified_at(dto.getVerified_at());
		entity.setApproved_by(dto.getApproved_by());
		entity.setApproved_at(dto.getApproved_at());
	}

	// =========================
	// SAFE HELPER METHOD
	// =========================
	private static Integer parseInteger(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return Integer.valueOf(value.trim());
		} catch (Exception e) {
			return null;
		}
	}
}
