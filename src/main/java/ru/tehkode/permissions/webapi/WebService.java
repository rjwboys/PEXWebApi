package ru.tehkode.permissions.webapi;

import java.io.IOException;

/**
 *
 * @author code
 */
public interface WebService {

	public void handle(WebRequest request) throws IOException;
	
}
