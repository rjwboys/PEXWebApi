/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.tehkode.permissions.webapi.services;

import java.util.ArrayList;
import java.util.List;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.webapi.AnnotatedWebService;
import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.annotations.Path;
import ru.tehkode.permissions.webapi.annotations.Return;


public class PEXUsersWebService extends AnnotatedWebService {
	
	@Path("/list")
	@Return("application/json")
	public Object getStatus(WebRequest r) {
		PermissionManager manager = PermissionsEx.getPermissionManager();
		
		List<String> users = new ArrayList<String>();
		
		for(PermissionUser user : manager.getUsers()) {
			users.add(user.getName());
		}
		
		return users;
	}
}
