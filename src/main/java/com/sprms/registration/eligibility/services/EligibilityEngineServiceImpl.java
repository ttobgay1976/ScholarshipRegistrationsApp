package com.sprms.registration.eligibility.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprms.registration.eligibility.repository.ScholarshipEligibilityRuleRepository;
import com.sprms.registration.eligibility.repository.ScholarshipEligibilitySubjectRepository;
import com.sprms.registration.frmbean.EligibilityResultDTO;
import com.sprms.registration.frmbean.StudentMarksDTO;
import com.sprms.registration.frmbean.StudentProfileDTO;
import com.sprms.registration.hbmbean.ScholarshipEligibilityRule;
import com.sprms.registration.hbmbean.ScholarshipEligibilitySubject;

@Service
@Transactional(readOnly = true)
public class EligibilityEngineServiceImpl implements EligibilityEngineService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(EligibilityEngineServiceImpl.class);

	// call repo
	private ScholarshipEligibilityRuleRepository _ruleRepository;
	private ScholarshipEligibilitySubjectRepository _subjectRepository;

	// constructor
	public EligibilityEngineServiceImpl(ScholarshipEligibilityRuleRepository ruleRepository,
			ScholarshipEligibilitySubjectRepository subjectRepository) {
		this._ruleRepository = ruleRepository;
		this._subjectRepository = subjectRepository;

	}

	@Override
	public EligibilityResultDTO checkEligibility(List<StudentProfileDTO> students) {

		EligibilityResultDTO result = new EligibilityResultDTO();

		if (students == null || students.isEmpty()) {

			result.setEligible(false);
			result.setMessage("Student information not found.");

			return result;
		}

		StudentProfileDTO student = students.get(0);

		List<ScholarshipEligibilityRule> rules = _ruleRepository.findByActiveTrue();

		if (rules.isEmpty()) {

			result.setEligible(false);
			result.setMessage("No active eligibility rules configured.");

			return result;
		}

		for (ScholarshipEligibilityRule rule : rules) {

			logger.info("Checking Rule : {}", rule.getScholarshipName());

			if (evaluateRule(rule, student)) {

				logger.info("Student qualified for {}", rule.getScholarshipName());

				result.setEligible(true);

				result.addEligibleRule(rule.getId(), rule.getScholarshipName());

			} else {

				logger.info("Student NOT qualified for {}", rule.getScholarshipName());

				result.addReason("Did not satisfy eligibility for " + rule.getScholarshipName());
			}
		}

		if (result.isEligible()) {

			result.setMessage("Student is eligible for scholarship registration.");

		} else {

			result.setMessage("You are not eligible for any scholarship.");
		}

		return result;
	}

	// EVALUTE RULES
	private boolean evaluateRule(ScholarshipEligibilityRule rule, StudentProfileDTO student) {

		logger.info("@@@Evaluating Rule : {}", rule.getScholarshipName());

// 1. Student must have all required subjects
		if (!validateSubjects(rule, student)) {
			return false;
		}

// 2. Student average must satisfy the rule
		if (!validateAverage(rule, student)) {
			return false;
		}

		return true;
	}

	// validate aveg
	private boolean validateAverage(ScholarshipEligibilityRule rule, StudentProfileDTO student) {

		List<ScholarshipEligibilitySubject> configuredSubjects = _subjectRepository.findByRule(rule);

		List<String> usedOthers = new ArrayList<>();

		double total = 0;
		int count = 0;

		for (ScholarshipEligibilitySubject config : configuredSubjects) {

			StudentMarksDTO studentMark;

			// OTHER SUBJECT
			if ("OTHERS".equalsIgnoreCase(config.getSubjectName())) {

				studentMark = findBestOtherSubject(student, configuredSubjects, rule, usedOthers);

				if (studentMark == null) {
					return false;
				}

				usedOthers.add(studentMark.getSubjectName().trim().toUpperCase());

			}

			// NORMAL SUBJECT
			else {

				studentMark = student.getSubjects().stream()
						.filter(m -> m.getSubjectName() != null
								&& m.getSubjectName().trim().equalsIgnoreCase(config.getSubjectName().trim()))
						.findFirst().orElse(null);

				if (studentMark == null) {
					return false;
				}

			}

			total += studentMark.getMark();
			count++;
		}

		if (count == 0) {
			return false;
		}

		double average = total / count;

		logger.info("------------------------------------");
		logger.info("Scholarship : {}", rule.getScholarshipName());
		logger.info("Average     : {}", average);
		logger.info("Required    : {}", rule.getMinimumAverage());
		logger.info("------------------------------------");

		return average >= rule.getMinimumAverage();
	}

	// VALIDATE SUBJECT
	private boolean validateSubjects(ScholarshipEligibilityRule rule, StudentProfileDTO student) {

		List<ScholarshipEligibilitySubject> configuredSubjects = _subjectRepository.findByRule(rule);

		List<String> usedOthers = new ArrayList<>();

		for (ScholarshipEligibilitySubject config : configuredSubjects) {

			StudentMarksDTO studentMark;

			// ==========================
			// OTHERS
			// ==========================
			if ("OTHERS".equalsIgnoreCase(config.getSubjectName())) {

				studentMark = findBestOtherSubject(student, configuredSubjects, rule, usedOthers);

				if (studentMark == null) {

					logger.warn("No eligible OTHER subject found.");

					return false;
				}

				usedOthers.add(studentMark.getSubjectName().trim().toUpperCase());

			}

			// ==========================
			// NORMAL SUBJECT
			// ==========================
			else {

				studentMark = student.getSubjects().stream()
						.filter(m -> m.getSubjectName() != null
								&& m.getSubjectName().trim().equalsIgnoreCase(config.getSubjectName().trim()))
						.findFirst().orElse(null);

				if (studentMark == null) {

					logger.warn("Subject {} not found.", config.getSubjectName());

					return false;
				}

			}

			// ==========================
			// MINIMUM MARK
			// ==========================

			if (config.getMinimumMark() != null && studentMark.getMark() < config.getMinimumMark()) {

				logger.warn("{} mark {} is below required {}", studentMark.getSubjectName(), studentMark.getMark(),
						config.getMinimumMark());

				return false;
			}

			logger.info("{} = {}", studentMark.getSubjectName(), studentMark.getMark());
		}

		return true;
	}

	// HELPER CLASS TO DETERMINE THE BEST SUBJECTS FOR THE MERIT CALCULATION
	private StudentMarksDTO findBestOtherSubject(StudentProfileDTO student,
			List<ScholarshipEligibilitySubject> configuredSubjects, ScholarshipEligibilityRule rule,
			List<String> usedSubjects) {

		// Configured subject names except OTHERS
		List<String> configured = configuredSubjects.stream().map(s -> s.getSubjectName().trim().toUpperCase())
				.filter(s -> !"OTHERS".equalsIgnoreCase(s)).toList();

		return student.getSubjects().stream()

				// Skip configured subjects
				.filter(m -> !configured.contains(m.getSubjectName().trim().toUpperCase()))

				// Skip already selected OTHER subjects
				.filter(m -> !usedSubjects.contains(m.getSubjectName().trim().toUpperCase()))

				// Exclude Dzongkha if configured
				.filter(m -> !(Boolean.TRUE.equals(rule.getExcludeDzongkha())
						&& "DZONGKHA".equalsIgnoreCase(m.getSubjectName())))

				// Highest mark wins
				.max(Comparator.comparing(StudentMarksDTO::getMark))

				.orElse(null);
	}

}