package com.sprms.registration.DTOMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.sprms.registration.frmbean.ScholarshipEligibilitySubjectDTO;
import com.sprms.registration.hbmbean.ScholarshipEligibilitySubject;


@Mapper(componentModel = "spring")
public interface ScholarshipEligibilitySubjectMapper {

	@Mapping(source = "rule.id", target = "ruleId")
	ScholarshipEligibilitySubjectDTO toDto(ScholarshipEligibilitySubject entity);

	@Mapping(source = "ruleId", target = "rule.id")
	ScholarshipEligibilitySubject toEntity(ScholarshipEligibilitySubjectDTO dto);

	@Mapping(target = "rule", ignore = true)
	void updateEntityFromDto(ScholarshipEligibilitySubjectDTO dto, @MappingTarget ScholarshipEligibilitySubject entity);

}
