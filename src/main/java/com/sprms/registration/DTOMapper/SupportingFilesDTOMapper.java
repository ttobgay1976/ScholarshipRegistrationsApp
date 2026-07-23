package com.sprms.registration.DTOMapper;

import java.util.List;
import java.util.stream.Collectors;

import com.sprms.registration.frmbean.SupportingFilesDTO;
import com.sprms.registration.hbmbean.SupportingFiles;

public class SupportingFilesDTOMapper {

	 // Entity -> DTO
    public static SupportingFilesDTO toDTO(SupportingFiles entity) {
        if (entity == null) {
            return null;
        }

        SupportingFilesDTO dto = new SupportingFilesDTO();

        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setFileLocation(entity.getFileLocation());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // List<Entity> -> List<DTO>
    public static List<SupportingFilesDTO> toDTOList(List<SupportingFiles> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(SupportingFilesDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    // DTO -> Entity
    public static SupportingFiles toEntity(SupportingFilesDTO dto) {
        if (dto == null) {
            return null;
        }

        SupportingFiles entity = new SupportingFiles();

        entity.setId(dto.getId());
        entity.setFileName(dto.getFileName());
        entity.setFileLocation(dto.getFileLocation());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }

    // Update existing entity from DTO
    public static void updateEntityFromDTO(SupportingFilesDTO dto, SupportingFiles entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setFileName(dto.getFileName());
        entity.setFileLocation(dto.getFileLocation());
        entity.setUpdatedAt(dto.getUpdatedAt());
    }
}
