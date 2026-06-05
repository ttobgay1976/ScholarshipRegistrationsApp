package com.sprms.registration.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.hbmbean.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository< StudentProfile, Long> {

	//THIS IS USE TO CHECK THE STUDENT INDEXNUNBER
	boolean existsByIndexNumber(String indexNumber);
}
