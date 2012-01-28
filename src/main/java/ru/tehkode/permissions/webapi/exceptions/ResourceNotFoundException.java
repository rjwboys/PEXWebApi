/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.exceptions;

public class ResourceNotFoundException extends WebApiException {

	protected String path;

	public ResourceNotFoundException() {
		super(404, "Not Found");
	}

	public ResourceNotFoundException(String path) {
		this();

		this.path = path;
	}
}
