package com.sprms.registration.DTOMapper;

import org.springframework.stereotype.Component;

import com.sprms.registration.frmbean.CitizenDetailDTO;
import com.sprms.registration.frmbean.ScholarshipRegistrationDTO;

@Component
public class CitizenRegistrationDTOMapper {

	public ScholarshipRegistrationDTO toRegistrationDTO(CitizenDetailDTO c) {
		
        if (c == null) {
            return null;
        }
   
        ScholarshipRegistrationDTO dto = new ScholarshipRegistrationDTO();
        
        dto.setId(c.getId());

        // 🔵 Identity
        dto.setCitizenId(c.getCid());

        // 🔵 Personal details
        dto.setFirstName(c.getFirstName());
        dto.setMiddleName(c.getMiddleName());
        dto.setLastName(c.getLastName());
        dto.setDateOfBirth(c.getDob());
        dto.setGender(c.getGender());

        // 🔵 Family
        dto.setFatherName(c.getFatherName());
        dto.setMotherName(c.getMotherName());
        
        // 🔵 Address
        dto.setDzongkhagId(c.getDzongkhagId());
        dto.setDzongkhagName(c.getDzongkhagName());
        dto.setGewogId(c.getGewogId());
        dto.setGewogName(c.getGewogName());
        dto.setVillageName(c.getVillageName());
        dto.setHouseNo(c.getHouseNo());
        dto.setThramNo(c.getThramNo());
        dto.setHouseholdNo(c.getHouseholdNo());

        return dto;
    }
}
