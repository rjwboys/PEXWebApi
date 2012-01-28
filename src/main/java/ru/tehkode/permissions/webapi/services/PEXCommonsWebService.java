/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.tehkode.permissions.webapi.services;

import java.util.Map;
import org.json.simple.JSONObject;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.webapi.AnnotatedWebService;
import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.annotations.Path;
import ru.tehkode.permissions.webapi.annotations.Return;


public class PEXCommonsWebService extends AnnotatedWebService {

	@Path("/status")
	@Return("application/json")
	public Map<String, String> getStatus(WebRequest r) {
		JSONObject obj = new JSONObject();
		
		obj.put("status", PermissionsEx.isAvailable());
		
		return obj;
	}
}
