package ru.tehkode.permissions.webapi.services;

import java.util.Arrays;
import java.util.Map;
import org.json.simple.JSONObject;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.annotations.Path;
import ru.tehkode.permissions.webapi.annotations.Return;

public abstract class PEXEntityWebService extends PEXWebService {

	@Path("/{name}/get/permissions/{world=}")
	@Return("application/json")
	public Object getEntityPermissions(WebRequest r) {
		PermissionEntity entity = this.getPermissionEntity(r.getArg("name"));
		
		return Arrays.asList(entity.getPermissions(r.getArg("world")));
	}
	
	@Path("/{name}/posttest")
	@Return("application/json")
	public Object getPostTest(WebRequest r) {
		System.out.println(new String(r.getRequest().array()));
		return r.getArgs();
	}
	
	@Path("/{name}/get/options/{world=}")
	@Return("application/json")
	public Object getEntityOptions(WebRequest r) {
		PermissionEntity entity = this.getPermissionEntity(r.getArg("name"));
		return Arrays.asList(entity.getOptions(r.getArg("world")));
	}
	
	@Path("/{name}/get/options/{world=}")
	@Return("application/json")
	public Object getAllEntityOptions(WebRequest r) {
		PermissionEntity entity = this.getPermissionEntity(r.getArg("name"));
		return Arrays.asList(entity.getOptions(r.getArg("world")));
	}

	protected abstract PermissionEntity getPermissionEntity(String name);
}
