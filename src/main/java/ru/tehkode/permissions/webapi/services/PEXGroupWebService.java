/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.services;

import java.util.ArrayList;
import java.util.List;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.annotations.Path;
import ru.tehkode.permissions.webapi.annotations.Return;

public class PEXGroupWebService extends PEXEntityWebService {

	@Override
	protected PermissionEntity getPermissionEntity(String name) {
		return PermissionsEx.getPermissionManager().getGroup(name);
	}
	
		@Path("/list")
	@Return("application/json")
	public Object getStatus(WebRequest r) {
		PermissionManager manager = PermissionsEx.getPermissionManager();

		List<String> groups = new ArrayList<String>();

		for (PermissionGroup user : manager.getGroups()) {
			groups.add(user.getName());
		}

		return groups;
	}
}
