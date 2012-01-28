/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.auth;

import org.bukkit.configuration.ConfigurationSection;
import ru.tehkode.permissions.webapi.WebRequest;

public class BasicAutheniticator implements WebAutheniticator {

	public ConfigurationSection config;

	public BasicAutheniticator(ConfigurationSection config) {
		this.config = config;
	}

	@Override
	public boolean autheniticate(WebRequest request) {
		return true;
	}
}
