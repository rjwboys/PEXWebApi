package ru.tehkode.permissions.webapi.auth;

import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;

public class AuthenticatorFactory {

	private final static Logger logger = Logger.getLogger("Minecraft");

	// @todo Make it in more dynamic way (reflection?)
	public static WebAutheniticator factory(ConfigurationSection section) {
		String type = section.getString("type", "token");

		if ("token".equalsIgnoreCase(type)) {
			return new TokenAuthenicator(section);
		}

		// unknown autheniticator
		logger.warning("Unknown autheniticator specified (\"" + type + "\"). No authenitication!");
		return new BasicAutheniticator(section);
	}
}
