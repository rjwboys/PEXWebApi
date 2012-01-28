/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.services;

import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PEXGroupWebService extends PEXEntityWebService {

	@Override
	protected PermissionEntity getPermissionEntity(String name) {
		return PermissionsEx.getPermissionManager().getGroup(name);
	}
}
