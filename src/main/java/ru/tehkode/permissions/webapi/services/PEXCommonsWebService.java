/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.tehkode.permissions.webapi.services;

import ru.tehkode.permissions.webapi.AnnotatedWebService;
import ru.tehkode.permissions.webapi.annotations.Path;


public class PEXCommonsWebService extends AnnotatedWebService {

	@Path("/user/{user}/permissions/{world=}/{tier=shit}")
	public String getPermissions() {
		return "PEX RUNNING LOUD AND CLEAR";
	}
}
