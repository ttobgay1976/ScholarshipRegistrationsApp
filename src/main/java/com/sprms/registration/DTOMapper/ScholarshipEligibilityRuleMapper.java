package com.sprms.registration.DTOMapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.sprms.registration.frmbean.ScholarshipEligibilityRuleDTO;
import com.sprms.registration.hbmbean.ScholarshipEligibilityRule;


@Mapper(componentModel = "spring", uses = ScholarshipEligibilitySubjectMapper.class)
public interface ScholarshipEligibilityRuleMapper {

	ScholarshipEligibilityRuleDTO toDto(ScholarshipEligibilityRule entity);

	ScholarshipEligibilityRule toEntity(ScholarshipEligibilityRuleDTO dto);

	void updateEntityFromDto(ScholarshipEligibilityRuleDTO dto, @MappingTarget ScholarshipEligibilityRule entity);
}
