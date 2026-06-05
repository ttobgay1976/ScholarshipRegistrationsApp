package com.sprms.registration.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sprms.registration.dao.ScholarsipRegistrationRepository;
import com.sprms.registration.dao.SupportingFilesRepository;
import com.sprms.registration.hbmbean.ScholarshipRegistration;
import com.sprms.registration.hbmbean.SupportingFiles;
import com.sprms.registration.utils.DateUtil;

@Service
public class SupportingFilesService {

	//LOGGER 
	private static final Logger logger = LoggerFactory.getLogger(SupportingFilesService.class);

	//CALL REPO
	private final SupportingFilesRepository _supportingFilesRepository;
	

	//constructor
	public SupportingFilesService(SupportingFilesRepository supportingFilesRepository,
			ScholarsipRegistrationRepository scholarsipRegistrationRepository) {

		this._supportingFilesRepository = supportingFilesRepository;
	}
	
	// file saving path and directories
	@Value("${file.upload-dir}")
	private String uploadDir;
	

	//save the supporting files
	@Transactional
	public void saveSupportingFiles(ScholarshipRegistration scholarshipRegistration, List<MultipartFile> uploadedFiles)
			throws IOException {

		if (uploadedFiles == null || uploadedFiles.isEmpty()) {
			return;
		}

		Path uploadDirPath = Paths.get(uploadDir, "scholarship", scholarshipRegistration.getId().toString())
				.toAbsolutePath().normalize();

		Files.createDirectories(uploadDirPath);

		for (MultipartFile file : uploadedFiles) {
			if (file != null && !file.isEmpty()) {

				String originalFileName = file.getOriginalFilename();
				String safeFileName = System.currentTimeMillis() + "_"
						+ originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

				Path filePath = uploadDirPath.resolve(safeFileName);
				file.transferTo(filePath.toFile());

				SupportingFiles doc = new SupportingFiles();
				doc.setFileName(safeFileName);
				doc.setFileLocation(filePath.toString());
				doc.setCreatedAt(DateUtil.getCurrentDateTime());
				doc.setScholarshipRegistration(scholarshipRegistration);

				_supportingFilesRepository.save(doc);
			}
		}
	}

//	get the file from the db passing the parent PK as FK
	@Transactional
	public Optional<SupportingFiles> getFiles(Long id) {

		logger.info("@@@Calling the getfile service..................");

		return _supportingFilesRepository.findById(id);
	}

	@Transactional
	public SupportingFiles getFileById(Long fileId) {
		// findById returns Optional<Files>
		return _supportingFilesRepository.findById(fileId)
				.orElseThrow(() -> new RuntimeException("File not found with id: " + fileId));
	}

}
