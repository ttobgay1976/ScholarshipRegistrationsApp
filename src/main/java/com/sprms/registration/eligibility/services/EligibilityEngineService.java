package com.sprms.registration.eligibility.services;

import java.util.List;

import com.sprms.registration.frmbean.EligibilityResultDTO;
import com.sprms.registration.frmbean.StudentProfileDTO;

public interface EligibilityEngineService {

	EligibilityResultDTO checkEligibility(List<StudentProfileDTO> students);

}
