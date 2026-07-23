package com.sprms.registration.eligibility.services;

import java.util.List;

import com.sprms.registration.frmbean.ScholarshipEligibilityRuleDTO;

public interface ScholarshipEligibilityRuleService {

	void save(ScholarshipEligibilityRuleDTO dto);

    ScholarshipEligibilityRuleDTO findById(Long id);

    void delete(Long id);

    void activate(Long id);

    void deactivate(Long id);

    List<ScholarshipEligibilityRuleDTO> findAll();
}
