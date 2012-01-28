package ru.tehkode.permissions.webapi.exceptions;

public class WebApiException extends RuntimeException {

	protected int statusCode;

	public WebApiException() {
		this("Internal Server Error");
	}

	public WebApiException(Throwable e) {
		this(500, "Internal Server Error", e);
	}

	public WebApiException(String message) {
		this(500, message);
	}

	public WebApiException(int statusCode, String message) {
		super(message);

		this.statusCode = statusCode;
	}

	public WebApiException(int statusCode, String message, Throwable e) {
		super(message, e);

		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
