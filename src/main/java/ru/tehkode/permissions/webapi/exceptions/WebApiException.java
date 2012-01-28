package ru.tehkode.permissions.webapi.exceptions;

public class WebApiException extends RuntimeException {

	protected int statusCode;
	protected boolean showTrace = true;

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
		this(statusCode, message, true);
	}

	public WebApiException(int statusCode, String message, boolean showTrace) {
		super(message);

		this.statusCode = statusCode;
		this.showTrace = showTrace;
	}

	public WebApiException(int statusCode, String message, Throwable e) {
		this(statusCode, message, true, e);
	}

	public WebApiException(int statusCode, String message, boolean showTrace, Throwable e) {
		super(message, e);

		this.statusCode = statusCode;
		this.showTrace = showTrace;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public boolean isShowTrace() {
		return showTrace;
	}
}
