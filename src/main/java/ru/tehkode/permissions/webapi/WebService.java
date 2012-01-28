/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.io.IOException;
import ru.tehkode.permissions.webapi.auth.WebAutheniticator;

/**
 *
 * @author code
 */
public interface WebService {

	public void handle(WebRequest request) throws IOException;
	
}
