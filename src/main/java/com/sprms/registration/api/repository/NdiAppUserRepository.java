package com.sprms.registration.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.frmDTO.AppUserDTO;
import com.sprms.registration.hbmbean.AppUser;

@Repository
public interface NdiAppUserRepository extends JpaRepository<AppUser, Long> {
    
	//find the appuser by CID number
	AppUser findByNdiIdNumber(String ndiIdNumber);
}
