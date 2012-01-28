/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.exceptions;

public class ServiceNotFoundException extends WebApiException {

	protected String path;

	public ServiceNotFoundException() {
		super(404, "Not Found");
	}

	public ServiceNotFoundException(String path) {
		this();

		this.path = path;
	}
}
