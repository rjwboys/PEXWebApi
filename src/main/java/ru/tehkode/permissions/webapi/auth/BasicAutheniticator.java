package ru.tehkode.permissions.webapi.auth;

import org.bukkit.configuration.ConfigurationSection;
import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.WebService;

public class BasicAutheniticator implements WebAutheniticator {

	public ConfigurationSection config;

	public BasicAutheniticator(ConfigurationSection config) {
		this.config = config;
	}

	@Override
	public boolean autheniticate(WebService service, WebRequest request) {
		return true;
	}
}
