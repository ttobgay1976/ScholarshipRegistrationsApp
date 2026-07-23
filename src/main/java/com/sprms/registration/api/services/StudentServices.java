package com.sprms.registration.api.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.registration.api.repository.StudentProfileRepository;
import com.sprms.registration.frmbean.StudentMarksDTO;
import com.sprms.registration.frmbean.StudentProfileDTO;
import com.sprms.registration.hbmbean.ScholarshipRegistration;
import com.sprms.registration.hbmbean.StudentMarks;
import com.sprms.registration.hbmbean.StudentProfile;

import jakarta.transaction.Transactional;

//THIS SERVICE WILL SAVE THE STUDENT AND MARKS FETCHED FROM THE BCSEA API
//CREATED 03/05/2026
//PLACE :HOME

@Service
public class StudentServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(StudentServices.class);

	// CALL THE REPO
	private final StudentProfileRepository _studentProfileRepository;

	// CONSTRUCTOR
	public StudentServices(StudentProfileRepository studentProfileRepository) {
		this._studentProfileRepository = studentProfileRepository;
	}

	@Transactional
	public String saveStudentProfiles(List<StudentProfileDTO> students, ScholarshipRegistration registration) {

		logger.info("@@@Calling saveStudentProfiles and student marks...................");

		// VALIDATION
		if (students == null || students.isEmpty()) {
			throw new RuntimeException("Student list cannot be null or empty");
		}

		List<StudentProfile> entities = new ArrayList<>();

		// LOOP EACH STUDENT

		for (StudentProfileDTO request : students) {

			if (request.getSubjects() == null || request.getSubjects().isEmpty()) {
				throw new RuntimeException("Subjects cannot be empty for index: " + request.getIndexNumber());
			}

			String streamCategory = determineStreamCategory(request.getStream(), request.getSubjects());

			
			// CREATE STUDENT

			StudentProfile student = new StudentProfile();
			student.setStudentName(request.getStudentName());
			student.setIndexNumber(request.getIndexNumber());
			student.setCidNumber(request.getCidNumber());
			student.setSchoolName(request.getSchoolName());
			student.setDzongkhag(request.getDzongkhag());
			student.setStream(request.getStream());
			
			//System.out.println("@@@Chheck for the value set Stream Cat :"+ streamCategory);
			
			student.setStreamCode(streamCategory);
			student.setExamYear(request.getExamYear());

			// IMPORTANT: SET REGISTRATION ID HERE
			student.setRegistration(registration);

			// MAP SUBJECTS → MARKS
			List<StudentMarks> marks = request.getSubjects().stream().map(sub -> {

				StudentMarks mark = new StudentMarks();
				mark.setSubjectName(sub.getSubjectName());
				mark.setMark(sub.getMark());
				mark.setGradeCode(sub.getGradeCode());

				// back reference
				mark.setStudentProfile(student);

				return mark;
			}).toList();

			student.setStudentMarks(marks);
			
			//added later
			student.setActive(false);

			entities.add(student);
		}

		// SAVE ALL (BULK INSERT)

		_studentProfileRepository.saveAll(entities);
		
		System.out.println("@@@Student Information save successfully.....................");

		return "Student profiles and marks saved successfully";
	}

	// HELPER METHOD
	private String determineStreamCategory(String stream, List<StudentMarksDTO> subjects) {

		if (stream == null) {
			return null;
		}

		// Normalize stream
		String normalizedStream = stream.trim().toUpperCase();

		// =========================
		// SCIENCE
		// =========================
		if ("SCIENCE".equals(normalizedStream)) {

			boolean hasMath = subjects.stream().anyMatch(s -> {

				String subject = s.getSubjectName();

				return subject != null && (subject.trim().equalsIgnoreCase("MATH")

						||

						subject.trim().equalsIgnoreCase("MATHEMATICS"));
			});

			boolean hasBiology = subjects.stream().anyMatch(s -> {

				String subject = s.getSubjectName();

				return subject != null && subject.trim().equalsIgnoreCase("BIOLOGY");
			});

			if (hasMath && hasBiology) {
				return "MED/ENG";
				//return "BOTH";
			}

			if (hasMath) {
				return "ENG";
			}

			return "MED";
		}

		// =========================
		// ARTS
		// =========================
		if ("ARTS".equals(normalizedStream)) {
			return "ARTS";
		}

		// =========================
		// COMMERCE
		// =========================
		if ("COMMERCE".equals(normalizedStream)) {
			return "COM";
		}

		// =========================
		// Default
		// =========================
		return "OTHER";
	}

}