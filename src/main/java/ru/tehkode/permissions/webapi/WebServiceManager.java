/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.util.Map;

/**
 *
 * @author code
 */
public interface WebServiceManager {
	
	public void start();
	public void stop();
	
	public void registerService(String basePath, WebService service);
	public void unregisterService(WebService service);
	
	public Map<String, WebService> getServices();
}
