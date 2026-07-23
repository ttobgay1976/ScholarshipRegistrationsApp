package com.sprms.registration.eligibility.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sprms.registration.applicationEnums.ApplicationStatus;
import com.sprms.registration.hbmbean.ScholarshipEligibilityRule;


@Repository
public interface ScholarshipEligibilityRuleRepository extends JpaRepository<ScholarshipEligibilityRule, Long> {

	List<ScholarshipEligibilityRule> findAllByOrderByExamYearDescScholarshipNameAsc();

	Optional<ScholarshipEligibilityRule> findByScholarshipNameAndExamYear(String scholarshipName, Integer examYear);

	boolean existsByScholarshipNameAndExamYear(String scholarshipName, Integer examYear);

	@Query("""
			    SELECT DISTINCT r
			    FROM ScholarshipEligibilityRule r
			    LEFT JOIN FETCH r.subjects
			    ORDER BY r.examYear DESC, r.scholarshipName ASC
			""")
	List<ScholarshipEligibilityRule> findAllWithSubjects();

	List<ScholarshipEligibilityRule> findByActiveTrue();
}
