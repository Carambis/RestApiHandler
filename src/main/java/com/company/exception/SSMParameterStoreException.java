package com.company.exception;

public class SSMParameterStoreException extends Exception {

	public SSMParameterStoreException() {

	}

	public SSMParameterStoreException(String message) {

		super(message);
	}

	public SSMParameterStoreException(String message, Throwable cause) {

		super(message, cause);
	}

	public SSMParameterStoreException(Throwable cause) {

		super(cause);
	}
}
