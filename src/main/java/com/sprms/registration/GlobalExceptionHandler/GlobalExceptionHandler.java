package com.sprms.registration.GlobalExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String DISPLAY_NDI_ERROR_PAGE = "ErrorPageForm";

	// ================= HTTP CLIENT ERRORS =================
	@ExceptionHandler(HttpClientErrorException.class)
	public String handleHttpClientError(HttpClientErrorException ex, Model model) {

		logger.error("HTTP CLIENT ERROR", ex);

		String message;

		switch (ex.getStatusCode().value()) {

		case 400 -> message = "Invalid request. Please check input data.";
		case 401 -> message = "Unauthorized access. Please login again.";
		case 404 -> message = "Requested data not found.";
		case 408 -> message = "Service timeout. Please try again.";
		default -> message = "Client error occurred. Please try again later.";
		}

		model.addAttribute("errorMessage", message);

		return DISPLAY_NDI_ERROR_PAGE;
	}

	// ================= GENERIC EXCEPTION =================
	@ExceptionHandler(Exception.class)
	public String handleGenericException(Exception ex, Model model) {

		logger.error("UNEXPECTED ERROR", ex);

		model.addAttribute("errorMessage", "Something went wrong. Please contact administrator or try again later.");

		return DISPLAY_NDI_ERROR_PAGE;
	}

	@ExceptionHandler(org.springframework.web.context.request.async.AsyncRequestNotUsableException.class)
	public void handleAsyncRequestNotUsable(Exception ex) {
		logger.warn("@@@Client disconnected from async/SSE response: {}", ex.getMessage());
	}
}