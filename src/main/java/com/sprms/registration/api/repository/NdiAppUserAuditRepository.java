package com.sprms.registration.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.frmDTO.NdiLoginAuditDTO;
import com.sprms.registration.hbmbean.NdiLoginAudit;

@Repository
public interface NdiAppUserAuditRepository extends JpaRepository<NdiLoginAudit, Long> {

    // 🔥 Spring will auto-implement this
    boolean existsByThid(String thid);

	void save(NdiLoginAuditDTO audit);
}
