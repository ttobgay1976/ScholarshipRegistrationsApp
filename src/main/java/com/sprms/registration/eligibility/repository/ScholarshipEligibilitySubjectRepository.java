package com.sprms.registration.eligibility.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.hbmbean.ScholarshipEligibilityRule;
import com.sprms.registration.hbmbean.ScholarshipEligibilitySubject;

@Repository
public interface ScholarshipEligibilitySubjectRepository extends JpaRepository<ScholarshipEligibilitySubject, Long> {

	List<ScholarshipEligibilitySubject> findByRuleId(Long ruleId);

	List<ScholarshipEligibilitySubject> findByRule(ScholarshipEligibilityRule rule);

}