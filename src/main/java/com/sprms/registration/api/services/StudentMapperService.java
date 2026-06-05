package com.sprms.registration.api.services;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sprms.registration.frmDTO.StudentApiResponseDTO;
import com.sprms.registration.frmDTO.StudentDTO;
import com.sprms.registration.frmDTO.StudentProfileDTO;
import com.sprms.registration.frmDTO.StudentMarksDTO;

@Service
public class StudentMapperService {

	// logger
	private static final Logger logger = LoggerFactory.getLogger(StudentMapperService.class);

	// COMMENTED
	public List<StudentProfileDTO> transform_OLD(StudentApiResponseDTO response) {

		logger.info("@@@Calling StudentMapperService proc....................");

		if (response == null || response.getStudents() == null || response.getStudents().getStudent() == null) {
			return Collections.emptyList();
		}

		List<StudentDTO> rawList = response.getStudents().getStudent();

		Map<String, List<StudentDTO>> grouped = rawList.stream()
				.collect(Collectors.groupingBy(StudentDTO::getIndexNumber, LinkedHashMap::new, Collectors.toList()));

		List<StudentProfileDTO> result = new ArrayList<>();

		for (List<StudentDTO> list : grouped.values()) {

			StudentDTO first = list.get(0);

			StudentProfileDTO dto = new StudentProfileDTO();

			dto.setStudentName(first.getStudentName());
			dto.setIndexNumber(first.getIndexNumber());
			dto.setCidNumber(first.getCidNumber());
			dto.setSchoolName(first.getSchoolName());
			dto.setDzongkhag(first.getDzongkhag());
			dto.setStream(first.getStream());

			String raw = first.getExamYear();
			LocalDate date = LocalDate.parse(raw.substring(0, 10));
			dto.setExamYear(date.getYear());

			List<StudentMarksDTO> subjects = list.stream().map(s -> {
				StudentMarksDTO sub = new StudentMarksDTO();
				sub.setSubjectName(s.getSubjectName());
				sub.setMark(s.getMark());
				sub.setGradeCode(s.getGradeCode());
				return sub;
			}).toList();

			dto.setSubjects(subjects);

			result.add(dto);
		}

		return result;
	}

	// NEW
	public List<StudentProfileDTO> transform(StudentApiResponseDTO response) {

		logger.info("@@@Calling StudentMapperService...");

		if (response == null || response.getStudents() == null || response.getStudents().getStudent() == null) {
			return Collections.emptyList();
		}

		List<StudentDTO> rawList = response.getStudents().getStudent();

		if (rawList == null || rawList.isEmpty()) {
			return Collections.emptyList();
		}

		Map<String, List<StudentDTO>> grouped = rawList.stream().filter(s -> s.getIndexNumber() != null)
				.collect(Collectors.groupingBy(StudentDTO::getIndexNumber, LinkedHashMap::new, Collectors.toList()));

		List<StudentProfileDTO> result = new ArrayList<>();

		for (List<StudentDTO> list : grouped.values()) {

			StudentDTO first = list.get(0);

			StudentProfileDTO dto = new StudentProfileDTO();

			dto.setStudentName(first.getStudentName());
			dto.setIndexNumber(first.getIndexNumber());
			dto.setCidNumber(first.getCidNumber());
			dto.setSchoolName(first.getSchoolName());
			dto.setDzongkhag(first.getDzongkhag());
			dto.setStream(first.getStream());

			/*
			 * String raw = first.getExamYear(); LocalDate date =
			 * LocalDate.parse(raw.substring(0, 10)); dto.setExamYear(date.getYear());
			 */

			dto.setExamYear(Integer.parseInt(first.getExamYear().substring(0, 4)));

			List<StudentMarksDTO> subjects = list.stream().filter(s -> s.getSubjectName() != null).map(s -> {
				StudentMarksDTO sub = new StudentMarksDTO();
				sub.setSubjectName(s.getSubjectName());
				sub.setMark(s.getMark());
				sub.setGradeCode(s.getGradeCode());
				return sub;
			}).collect(Collectors.toList());

			dto.setSubjects(subjects);

			result.add(dto);
		}

		return result;
	}
}
