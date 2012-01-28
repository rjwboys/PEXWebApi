/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.bukkit;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.webapi.WebServiceManager;
import ru.tehkode.permissions.webapi.SimpleWebServiceManager;
import ru.tehkode.permissions.webapi.services.PEXCommonsWebService;
import ru.tehkode.permissions.webapi.services.PEXUsersWebService;

public class PEXWebApi extends JavaPlugin {

	protected final static Logger logger = Logger.getLogger("Minecraft");
	protected WebServiceManager service;

	@Override
	public void onEnable() {
		logger.info("[PEXWebApi] Enabling PermissionsEx Web API!");
		logger.info("[PEXWebApi] Checking for PermissionsEx");

		if (!PermissionsEx.isAvailable()) {
			logger.severe("[PEXWebApi] PermissionsEx is not available. Make sure PermissionsEx init/running properly.");
			this.getPluginLoader().disablePlugin(this);
			return;
		}

		try {
			service = new SimpleWebServiceManager(9000);

			service.start();
		} catch (Throwable e) {
			logger.severe("[PEXWebApi] Failed to start HTTP server: " + e.getMessage());
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		service.registerService("/pex", new PEXCommonsWebService());
		service.registerService("/pex/user", new PEXUsersWebService());

		logger.info("[PEXWebApi] Successfully started! Listeing on http://localhost:9000/");
	}

	@Override
	public void onDisable() {
		logger.info("[PEXWEBAPI] Shutdown...");
		if (this.service != null) {
			this.service.stop();
		}
		logger.info("[PEXWEBAPI] Succesfuly disabled!");
	}
}
