package com.sprms.registration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.hbmbean.SupportingFiles;

@Repository
public interface SupportingFilesRepository extends JpaRepository<SupportingFiles, Long> {

}
