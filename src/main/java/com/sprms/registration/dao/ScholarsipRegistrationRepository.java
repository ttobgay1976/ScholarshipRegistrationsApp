package com.sprms.registration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.hbmbean.ScholarshipRegistration;

@Repository
public interface ScholarsipRegistrationRepository extends JpaRepository<ScholarshipRegistration, Long> {

}
