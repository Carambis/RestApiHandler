package com.company.exception;

public class CompressionException extends Exception {

	public CompressionException() {

	}

	public CompressionException(String message) {

		super(message);
	}

	public CompressionException(String message, Throwable cause) {

		super(message, cause);
	}

	public CompressionException(Throwable cause) {

		super(cause);
	}
}
