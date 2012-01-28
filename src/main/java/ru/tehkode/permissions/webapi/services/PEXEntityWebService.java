package ru.tehkode.permissions.webapi.services;

import java.util.Arrays;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.annotations.Path;

public abstract class PEXEntityWebService extends PEXWebService {

	@Path("{name}/get/permissions/{world=}")
	public Object getEntityPermissions(WebRequest r) {
		PermissionEntity entity = this.getPermissionEntity(r.getArg("name"));		
		return Arrays.asList(entity.getPermissions(r.getArg("world")));
	}

	protected abstract PermissionEntity getPermissionEntity(String name);
}
