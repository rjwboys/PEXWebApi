package ru.tehkode.permissions.webapi.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import ru.tehkode.permissions.webapi.WebRequest;

public class TokenAuthenicator extends BasicAutheniticator {
	private final static Logger logger = Logger.getLogger("Minecraft");
	
	protected String token = null;

	public TokenAuthenicator(ConfigurationSection config) {
		super(config);

		this.token = config.getString("token");
		if (this.token == null) {
			logger.severe("[PEXWebApi] No authenitication token found! Generating...");
			this.config.set("token", this.token = this.generateToken());
		}
	}

	private String generateToken() {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA1");
			String newToken = new String(sha.digest(Double.toString(System.nanoTime() * Math.random()).getBytes()));

			logger.warning("[PEXWebApi] Generated new authenitcation token. Token is written to \"/plugins/PEXWebApi/config.yml\"!");

			return newToken;
		} catch (NoSuchAlgorithmException e) {
			logger.severe("Can't generate token. Please write down token by yourself into \"/plugins/PEXWebApi/config.yml\"!");
		}

		return "!!!CHANGETHIS!!!"; // change this! :D
	}

	@Override
	public boolean autheniticate(WebRequest request) {
		return (request.isArgSet("token") && this.token.equalsIgnoreCase(request.getArg("token")));
	}
}
