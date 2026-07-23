package com.sprms.registration.DTOMapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.sprms.registration.frmbean.NdiLoginAuditDTO;
import com.sprms.registration.hbmbean.NdiLoginAudit;

@Component
public class NdiLoginAuditDTOMapper {

	public NdiLoginAuditDTO toDTO(NdiLoginAudit e) {
		if (e == null)
			return null;

		NdiLoginAuditDTO d = new NdiLoginAuditDTO();
		d.setId(e.getId());
		d.setThid(e.getThid());
		d.setNdiIdNumber(e.getNdiIdNumber());
		d.setLoginTime(e.getLoginTime());
		d.setStatus(e.getStatus());

		return d;
	}

	public NdiLoginAudit toEntity(NdiLoginAuditDTO d) {
		if (d == null)
			return null;

		NdiLoginAudit e = new NdiLoginAudit();
		e.setId(d.getId());
		e.setThid(d.getThid());
		e.setNdiIdNumber(d.getNdiIdNumber());
		e.setLoginTime(d.getLoginTime() != null ? d.getLoginTime() : LocalDateTime.now());
		e.setStatus(d.getStatus());

		return e;
	}

}
