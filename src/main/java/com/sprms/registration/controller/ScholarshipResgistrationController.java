package com.sprms.registration.controller;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.registration.DTOMapper.CitizenRegistrationDTOMapper;
import com.sprms.registration.api.repository.NdiAppUserRepository;
import com.sprms.registration.api.services.DcrcAuthNCitizenServices;
import com.sprms.registration.api.services.NdiUserSessionService;
import com.sprms.registration.exception.BcseaApiException;
import com.sprms.registration.exception.BusinessException;
import com.sprms.registration.exception.EligibilityException;
import com.sprms.registration.frmbean.CitizenDetailDTO;
import com.sprms.registration.frmbean.ScholarshipRegistrationDTO;
import com.sprms.registration.services.ScholarsipRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/scholarship")
public class ScholarshipResgistrationController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipResgistrationController.class);

	// setting the static variable for the form call
	private static String DISPLAY_STUDENT_REGISTRATION_VIS_NDI_FRM = "ScholarshipRegistrationViaNDIFrm";
	private static String DISPLAY_STUDENT_REGISTRATION_VIA_DCRC_FRM = "ScholarshipRegistrationViaDCRCFrm";
	private static String DISPLAY_THANK_YOU_PAGE = "ThankFrm";

	// call the repository or service
	private final ScholarsipRegistrationService _scholarsipRegistrationService;
	private final DcrcAuthNCitizenServices _dcrcAuthNCitizenServices;
	private final CitizenRegistrationDTOMapper _citizenRegistrationDTOMapper;

	// constructor to initialize the above declaration
	public ScholarshipResgistrationController(ScholarsipRegistrationService scholarsipRegistrationService,
			NdiAppUserRepository ndiAppUserRepository, DcrcAuthNCitizenServices dcrcAuthNCitizenServices,
			CitizenRegistrationDTOMapper citizenRegistrationDTOMapper, NdiUserSessionService ndiUserSessionService) {
		this._scholarsipRegistrationService = scholarsipRegistrationService;
		this._dcrcAuthNCitizenServices = dcrcAuthNCitizenServices;
		this._citizenRegistrationDTOMapper = citizenRegistrationDTOMapper;
	}

	// THIS FORM WILL BE OPEN USING NDI APP
	// THIS IS USE FOR NDI LOGIN
	@GetMapping("/registrationfrm")
	public String getStudentRegistrationFrm(Model model, HttpSession session, HttpServletRequest request) {

		logger.info("@@@Calling the Student Registration frm...................");

		ScholarshipRegistrationDTO formDto = new ScholarshipRegistrationDTO(); // 🔥 default

		// GET THE CID FROM THE NDI SESSION PASSED
		String citizenID = (String) session.getAttribute("CID");

		// CALL DCRC API
		CitizenDetailDTO citizenDetails = null;

		if (citizenID != null && !citizenID.isBlank()) {

			String token = _dcrcAuthNCitizenServices.getAccessToken();
			citizenDetails = _dcrcAuthNCitizenServices.getCitizenDetils(citizenID, token);

			if (citizenDetails != null) {
				formDto = _citizenRegistrationDTOMapper.toRegistrationDTO(citizenDetails);
			}
		}

		// ADD TO MODEL (IMPORTANT)
		model.addAttribute("scholarshipRegistrationdto", formDto);

		return DISPLAY_STUDENT_REGISTRATION_VIS_NDI_FRM;
	}

	// get the Registration information and save
	// date 09/04/2026
	@PostMapping("/addregistration")
	public String addScholarshilRegistration(
			@ModelAttribute("scholarshipRegistrationdto") ScholarshipRegistrationDTO dto,
			@RequestParam("files") List<MultipartFile> files, RedirectAttributes redirectAttributes, Model model) {

		logger.info("@@@Calling the addScholarshilRegistration.......................... ");

		try {

			boolean isEdit = dto.getId() != null;

			ScholarshipRegistrationDTO savedRegistration = _scholarsipRegistrationService.saveScholarshipDetails(dto);

			// REDIRECT THE INFORMATION TO FORM TO DISPLAY/SHOW IN THE FORM
			redirectAttributes.addFlashAttribute("message", isEdit ? "Scholarship updated successfully."
					: "You have successfully registered for the scholarship progran.");
			redirectAttributes.addFlashAttribute("name",
					savedRegistration.getFirstName() + " " + savedRegistration.getLastName());
			redirectAttributes.addFlashAttribute("indexNo", savedRegistration.getIndexNumber());
			redirectAttributes.addFlashAttribute("regNo", savedRegistration.getId());

		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("successMessage", "File upload failed: " + e.getMessage());

			System.out.println("@@@Check the error :" + e.getStackTrace());

			return "redirect:/scholarship/registrationfrm";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("successMessage" + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

			return "redirect:/scholarship/registrationfrm";
		}

		return "redirect:/scholarship/thankpage";
	}

	/*
	 * -----------------------------------------------------------------------------
	 */
	// THIS FORM IS FOR THE SCHOLARSHIP REGISTRATION WITHOUT NDI
	// @GetMapping("/dcrc-registrationfrm")
	@GetMapping("/registration")
	public String getDCRCStudentRegistrationFrm(Model model) {

		logger.info("@@@Calling the manual CID entry DCRC info  frm...................");

		ScholarshipRegistrationDTO formDto = new ScholarshipRegistrationDTO();

		// ADD TO MODEL (IMPORTANT)
		model.addAttribute("scholarshipRegistrationdto", formDto);

		return DISPLAY_STUDENT_REGISTRATION_VIA_DCRC_FRM;
	}

	// THIS IS USE FOR THE DCRC API CALL SAVE
	@PostMapping("/dcrc-registration")
	public String addDCRCScholarshipRegistration(
			@ModelAttribute("scholarshipRegistrationdto") ScholarshipRegistrationDTO dto,
			RedirectAttributes redirectAttributes, Model model) {

		logger.info("@@@Calling the manual addScholarshilRegistration.......................... ");

		try {

			boolean isEdit = dto.getId() != null;

			// call the save method
			ScholarshipRegistrationDTO savedRegistration = _scholarsipRegistrationService.saveScholarshipDetails(dto);

			// REDIRECT THE INFORMATION TO FORM TO DISPLAY/SHOW IN THE FORM
			redirectAttributes.addFlashAttribute("message", isEdit ? "Scholarship updated successfully."
					: "You have successfully registered for the scholarship progran.");
			redirectAttributes.addFlashAttribute("name",
					savedRegistration.getFirstName() + " " + savedRegistration.getLastName());
			redirectAttributes.addFlashAttribute("indexNo", savedRegistration.getIndexNumber());
			redirectAttributes.addFlashAttribute("regNo", savedRegistration.getId());
			redirectAttributes.addFlashAttribute("cidNo", savedRegistration.getCitizenId());

		} catch (EligibilityException e) {

			logger.warn("Eligibility Error", e);

			redirectAttributes.addFlashAttribute("errorType", "ELIGIBILITY");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

			return "redirect:/scholarship/registration";

		} catch (BcseaApiException e) {

			logger.error("BCSEA API Error", e);

			redirectAttributes.addFlashAttribute("errorType", "API");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

			return "redirect:/scholarship/registration";

		} catch (IllegalArgumentException e) {

			logger.warn("Validation Error", e);

			redirectAttributes.addFlashAttribute("errorType", "VALIDATION");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

			return "redirect:/scholarship/registration";

		} catch (IOException e) {

			logger.error("File Upload Error", e);

			redirectAttributes.addFlashAttribute("errorType", "FILE_UPLOAD");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

			return "redirect:/scholarship/registration";

		} catch (BusinessException e) {

			logger.warn("Business Validation Error", e);

			redirectAttributes.addFlashAttribute("errorType", "BUSINESS");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

			return "redirect:/scholarship/registration";

		} catch (Exception e) {

			logger.error("Scholarship Registration Error", e);

			redirectAttributes.addFlashAttribute("errorType", "SYSTEM");
			redirectAttributes.addFlashAttribute("errorMessage",
					"An unexpected error occurred. Please try again later.");

			return "redirect:/scholarship/registration";
		}

		return "redirect:/scholarship/thankpage";
	}

	// display thank you page after successful registration
	// date 10/04/2026
	@GetMapping("/thankpage")
	public String getThankYouPage(Model model) {

		logger.info("@@@Calling the getThankYouPage......................");

		return DISPLAY_THANK_YOU_PAGE;
	}

}
