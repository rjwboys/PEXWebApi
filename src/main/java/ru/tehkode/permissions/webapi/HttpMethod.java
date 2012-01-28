/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author code
 */
public enum HttpMethod {

	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE");
	
	protected final static Map<String, HttpMethod> map = new HashMap<String, HttpMethod>();

	static {
		for (HttpMethod method : HttpMethod.values()) {
			map.put(method.getMethod().toLowerCase(), method);
		}
	}
	private String method;

	private HttpMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public static HttpMethod fromMethod(String method) {
		return map.get(method.toLowerCase());
	}
}
