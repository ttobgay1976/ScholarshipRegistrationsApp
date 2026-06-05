package com.sprms.registration.exception;

public class BcseaApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BcseaApiException(String message) {
		super(message);
	}

	public BcseaApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
