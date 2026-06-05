package com.sprms.registration.api.services;

import org.springframework.stereotype.Service;

import com.sprms.registration.api.repository.StudentProfileRepository;
import com.sprms.registration.exception.BusinessException;

@Service
public class ValidateDuplicateStudentIndexNumber {

	//IMPOR REPO
	private final StudentProfileRepository _studentProfileRepository;
	
	//CONSTRUCTOR
	public ValidateDuplicateStudentIndexNumber(StudentProfileRepository studentProfileRepository) {
		this._studentProfileRepository=studentProfileRepository;
	}

	public void validateDuplicateIndex(String indexNumber) {

		if (_studentProfileRepository.existsByIndexNumber(indexNumber)) {
			throw new BusinessException("Student already registered with index number: " + indexNumber);
		}

	}
}
