package com.sprms.registration.GlobalExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class WebExceptionHandler {

	@ExceptionHandler(Exception.class)
	public String handle(Exception ex, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong!");

		return "redirect:/form";
	}

}
