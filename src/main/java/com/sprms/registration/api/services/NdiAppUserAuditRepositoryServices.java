package com.sprms.registration.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.registration.api.repository.NdiAppUserAuditRepository;
import com.sprms.registration.frmDTO.VerifiedUserDTO;
import com.sprms.registration.hbmbean.NdiLoginAudit;
import com.sprms.registration.utils.DateUtil;

@Service
public class NdiAppUserAuditRepositoryServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiAppUserAuditRepositoryServices.class);

	// call the repository
	private final NdiAppUserAuditRepository _ndiAppUserAuditRepository;

	// constructor
	public NdiAppUserAuditRepositoryServices(NdiAppUserAuditRepository ndiAppUserAuditRepository) {
		this._ndiAppUserAuditRepository = ndiAppUserAuditRepository;
	}

	public void saveAuditLog(String thid, VerifiedUserDTO user) {

		logger.info("@@@Calling the saveAuditLog proc..........................");

		// ✅ Safety check
		if (thid == null || user == null) {
			return;
		}

		NdiLoginAudit audit = new NdiLoginAudit();

		audit.setThid(thid);
		audit.setNdiIdNumber(user.getIdNumber());
		audit.setLoginTime(DateUtil.getCurrentDateTime());
		audit.setStatus("SUCCESS");

		_ndiAppUserAuditRepository.save(audit);
	}
}
