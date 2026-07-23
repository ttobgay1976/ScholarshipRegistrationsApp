package com.sprms.registration.eligibility.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprms.registration.DTOMapper.ScholarshipEligibilityRuleMapper;
import com.sprms.registration.eligibility.repository.ScholarshipEligibilityRuleRepository;
import com.sprms.registration.frmbean.ScholarshipEligibilityRuleDTO;
import com.sprms.registration.frmbean.ScholarshipEligibilitySubjectDTO;
import com.sprms.registration.hbmbean.ScholarshipEligibilityRule;
import com.sprms.registration.hbmbean.ScholarshipEligibilitySubject;
import com.sprms.registration.utils.GetCurrentUserUtil;


@Service
@Transactional
public class ScholarshipEligibilityRuleServiceImpl implements ScholarshipEligibilityRuleService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipEligibilityRuleServiceImpl.class);

	// import Repo
	//private final ScholarshipEligibilitySubjectRepository scholarshipEligibilitySubjectRepository;
	private final ScholarshipEligibilityRuleMapper mapper;
	private final ScholarshipEligibilityRuleRepository  repository;

	// constructor
	public ScholarshipEligibilityRuleServiceImpl(ScholarshipEligibilityRuleRepository repository,
			ScholarshipEligibilityRuleMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public void save(ScholarshipEligibilityRuleDTO dto) {
		
		logger.info("@@@Calling this  void Save method for ScholarshipEligibilityRule.................");

		validateDuplicate(dto);

		ScholarshipEligibilityRule entity;

		if (dto.getId() == null) {

			entity = mapper.toEntity(dto);

			entity.setCreatedAt(LocalDateTime.now());
			entity.setCreatedBy(GetCurrentUserUtil.getCurrentUsername());

		} else {

			entity = repository.findById(dto.getId())
					.orElseThrow(() -> new RuntimeException("Eligibility Rule not found"));

			entity.setScholarshipName(dto.getScholarshipName());
			entity.setExamYear(dto.getExamYear());
			entity.setMinimumAverage(dto.getMinimumAverage());
			entity.setBestSubjectCount(dto.getBestSubjectCount());
			entity.setExcludeDzongkha(dto.getExcludeDzongkha());
			entity.setRemarks(dto.getRemarks());
			entity.setActive(dto.getActive());

			entity.setUpdatedAt(LocalDateTime.now());
			entity.setUpdatedBy(GetCurrentUserUtil.getCurrentUsername());

			entity.getSubjects().clear();

			if (dto.getSubjects() != null) {

				for (ScholarshipEligibilitySubjectDTO childDto : dto.getSubjects()) {

					ScholarshipEligibilitySubject child = new ScholarshipEligibilitySubject();

					child.setRule(entity);
					child.setSubjectName(childDto.getSubjectName());
					child.setMinimumMark(childDto.getMinimumMark());
					child.setRequiredSubject(childDto.getRequiredSubject());

					entity.getSubjects().add(child);

				}

			}

			repository.save(entity);

			return;
		}

		if (entity.getSubjects() != null) {

			entity.getSubjects().forEach(s -> s.setRule(entity));

		}

		repository.save(entity);

	}

	@Override
	@Transactional(readOnly = true)
	public ScholarshipEligibilityRuleDTO findById(Long id) {
		
		logger.info("@@@Calling this  void Save method for findById.................");

		ScholarshipEligibilityRule entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Eligibility Rule not found"));

		return mapper.toDto(entity);

	}

	@Override
	@Transactional(readOnly = true)
	public List<ScholarshipEligibilityRuleDTO> findAll() {

		logger.info("@@@Calling this  void Save method for findAll.................");
		
		return repository.findAllByOrderByExamYearDescScholarshipNameAsc().stream().map(mapper::toDto).toList();

	}

	@Override
	public void delete(Long id) {

		logger.info("@@@Calling this  void Save method for delete.................");
		
		ScholarshipEligibilityRule entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Eligibility Rule not found"));

		repository.delete(entity);

	}

	@Override
	public void activate(Long id) {

		logger.info("@@@Calling this  void Save method for activate.................");
		
		ScholarshipEligibilityRule entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Eligibility Rule not found"));

		entity.setActive(true);

		entity.setUpdatedAt(LocalDateTime.now());
		entity.setUpdatedBy(GetCurrentUserUtil.getCurrentUsername());

		repository.save(entity);

	}

	@Override
	public void deactivate(Long id) {

		logger.info("@@@Calling this  void Save method for deactivate.................");
		
		ScholarshipEligibilityRule entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Eligibility Rule not found"));

		entity.setActive(false);

		entity.setUpdatedAt(LocalDateTime.now());
		entity.setUpdatedBy(GetCurrentUserUtil.getCurrentUsername());

		repository.save(entity);

	}

	private void validateDuplicate(ScholarshipEligibilityRuleDTO dto) {

		logger.info("@@@Calling this  void Save method for validateDuplicate.................");
		
		Optional<ScholarshipEligibilityRule> existing = repository
				.findByScholarshipNameAndExamYear(dto.getScholarshipName(), dto.getExamYear());

		if (existing.isPresent()) {

			if (dto.getId() == null || !existing.get().getId().equals(dto.getId())) {

				throw new RuntimeException("Eligibility rule already exists for this scholarship and exam year.");

			}

		}

	}
	

}