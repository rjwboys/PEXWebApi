/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONObject;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.annotations.Path;
import ru.tehkode.permissions.webapi.annotations.Return;
import ru.tehkode.permissions.webapi.exceptions.ResourceNotFoundException;

public class PEXCommonsWebService extends PEXWebService {

	@Path("/webui/{file=webui.html}")
	@Return("text/plain")
	public Object getWebUI(WebRequest r) throws IOException {
		String fileName = r.getArg("file");
		InputStream io = this.getClass().getResourceAsStream("/webui/" + fileName);
		if (io == null) {
			throw new ResourceNotFoundException();
		}
		
		InputStreamReader reader = new InputStreamReader(io);

		ReadableByteChannel inc = Channels.newChannel(io);
		WritableByteChannel ouc = Channels.newChannel(r.getOutputStream());

		ByteBuffer buffer = ByteBuffer.allocate(1024);

		while (inc.read(buffer) != -1) {
			buffer.flip();
			ouc.write(buffer);
			buffer.compact();
		}

		buffer.flip();

		while (buffer.hasRemaining()) {
			ouc.write(buffer);
		}

		return null;
	}

	@Path("/get/status")
	@Return("application/json")
	public Object getStatus(WebRequest r) {
		JSONObject obj = new JSONObject();

		obj.put("status", PermissionsEx.isAvailable());

		return obj;
	}
	
	@Path("/call/reload")
	@Return("application/json")
	public Object callReload(WebRequest r) {
		PermissionsEx.getPermissionManager().reset();
		JSONObject obj = new JSONObject();
		obj.put("reload", true);
		return obj;
	}

	@Path("/get/permissions")
	@Return("application/json")
	public Object getSuperPerms(WebRequest r) {
		JSONObject obj = new JSONObject();

		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			PluginDescriptionFile pdf = plugin.getDescription();
			JSONObject pluginObj = new JSONObject();
			{
				pluginObj.put("description", pdf.getDescription());
				pluginObj.put("version", pdf.getVersion());
				JSONObject pluginPermissions = new JSONObject();
				{
					for (Permission permission : pdf.getPermissions()) {
						JSONObject permissionObj = new JSONObject();
						{
							permissionObj.put("description", permission.getDescription());
							permissionObj.put("default", permission.getDefault().toString());
							if (!permission.getChildren().isEmpty()) {
								permissionObj.put("children", permission.getChildren());
							}
						}
						pluginPermissions.put(permission.getName(), permissionObj);
					}
				}
				pluginObj.put("permissions", pluginPermissions);
			}
			obj.put(pdf.getName(), pluginObj);
		}

		return obj;
	}
}
