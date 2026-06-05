package com.sprms.registration.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.hbmbean.StudentMarks;

@Repository
public interface StudentMarkRepository extends JpaRepository< StudentMarks, Long> {

}
