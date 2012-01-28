package ru.tehkode.permissions.webapi.exceptions;


public class AccessDeniedException extends WebApiException {

	public AccessDeniedException() {
		super(403, "Forbidden", false);
	}	

}
