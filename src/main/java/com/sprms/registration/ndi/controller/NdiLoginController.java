package com.sprms.registration.ndi.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.sprms.registration.api.services.NdiAuthServices;
import com.sprms.registration.api.services.NdiDataExtractorServices;
import com.sprms.registration.api.services.NdiLoginStoreServices;
import com.sprms.registration.api.services.NdiRestServices;
import com.sprms.registration.api.services.PostLoginService;
import com.sprms.registration.applicationEnums.NDIStatus;
import com.sprms.registration.frmDTO.AppUserDTO;
import com.sprms.registration.frmDTO.ProofRequestResponseDTO;
import com.sprms.registration.frmDTO.VerifiedUserDTO;
import jakarta.servlet.http.HttpSession;

//this controller is use for NDI API call
//those following urls are called from the NDIScan form js
//created dt 21/04/2026

@Controller
@RequestMapping("/ndi")
public class NdiLoginController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiLoginController.class);

	// CALL THE REPO
	private final NdiRestServices _ndiRestServices;
	private final PostLoginService _postLoginService;

	// setting the static variable for the form call
	private static String DISPLAY_NDI_SCAN_PAGE = "NdiScanForm";
	private static String DISPLAY_WAITING_PAGE = "NdiWaitingForm";

	// THIS IS THE CONSTRUCTOR TO INITIALIZE THE PRIVATE FINAL DECLARATION
	public NdiLoginController(NdiAuthServices ndiApiTokenServices, NdiRestServices ndiRestServices,
			NdiDataExtractorServices ndiDataExtractorServices, PostLoginService postLoginService) {
		this._ndiRestServices = ndiRestServices;
		this._postLoginService = postLoginService;
	}

	// THIS IS THE NDI LOGIN PAGE
	@GetMapping("/login")
	public String getAccessToken(Model model, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the NDI API to get token......................");

		String token = _ndiRestServices.getAccessToken();

		ProofRequestResponseDTO response = _ndiRestServices.createProofRequest(token);

		// this the return value to form
		model.addAttribute("qrUrl", response.getData().getProofRequestURL());
		model.addAttribute("deepLink", response.getData().getDeepLinkURL());
		model.addAttribute("threadId", response.getData().getProofRequestThreadId());

		// System.out.println("@@@Print ThredId :" +
		// response.getData().getProofRequestThreadId());

		return DISPLAY_NDI_SCAN_PAGE;
	}

	// NDI POST LOGIN CALLED FROM NDISCAN FORM JS
	@GetMapping("/post-login")
	public String postLogin(@RequestParam String thid, HttpSession session) {

		System.out.println("🔥 POST LOGIN CALLED FOR THID: " + thid);

		try {
			VerifiedUserDTO user = NdiLoginStoreServices.getUser(thid);

			if (user == null) {
				return "redirect:/ndi/login";
			}

			session.setAttribute("USER", user);
			session.setAttribute("NDI_STATUS", NDIStatus.VERIFIED);
			session.setAttribute("CID", user.getIdNumber());
			session.setAttribute("USERNAME", user.getFullName());
			session.setAttribute("DOB", user.getDateOfBirth());

			NdiLoginStoreServices.remove(thid);

			// ✅ Mark READY LAST (signal final state)
			_postLoginService.markReady(thid);

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/ndi/login";
		}

		return "redirect:/scholarship/registrationfrm";
	}

	@GetMapping("/check-post-login-ready")
	@ResponseBody
	public Map<String, Boolean> checkReadyStatus(@RequestParam String thid) {

		logger.info("@@@Calling the checkReady proc...................");

		boolean ready = _postLoginService.isReady(thid);

		System.out.println("POST LOGIN HIT: " + thid);

		return Map.of("ready", true);
	}

	// THIS IS THE WAITING PAGE BEFORE CALLING THE POST-LOGIN FORM
	// THERE IS TIME TAKEN AS IT WAIT TO REGISTRATION FORM
	// CREATED : 30/04/2026

	@GetMapping("/waiting")
	public String waitingPage(@RequestParam String thid, Model model) {

		logger.info("@@@Calling the waitingform ........................");

		model.addAttribute("thid", thid);

		return DISPLAY_WAITING_PAGE;
	}

	// NDI STATUS CALLED FROM NDISCAN FORM JS
	@GetMapping("/status")
	public ResponseEntity<Map<String, String>> getStatus(@RequestParam String thid) {

		logger.info("@@@Calling the getStatus proc.................");

		NDIStatus status = _ndiRestServices.getByThreadId(thid);

		Map<String, String> response = new HashMap<>();
		response.put("status", status.name());

		return ResponseEntity.ok(response);
	}
}
