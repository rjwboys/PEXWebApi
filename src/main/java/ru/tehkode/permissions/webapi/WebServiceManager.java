/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.io.IOException;
import java.util.Map;
import ru.tehkode.permissions.webapi.auth.WebAutheniticator;

/**
 *
 * @author code
 */
public interface WebServiceManager {

	public void start() throws IOException;

	public void stop();

	public void registerService(String basePath, WebService service);

	public void unregisterService(WebService service);

	public Map<String, WebService> getServices();

	public WebAutheniticator getAutheniticator();
}
