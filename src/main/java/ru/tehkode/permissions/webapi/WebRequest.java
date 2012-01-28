/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 *
 * @author code
 */
public interface WebRequest {
	
	public String getBasePath();
	
	public String getRelativePath();

	public URI getRequestURL();

	public Map<String, List<String>> getRequestHeaders();

	public Map<String, List<String>> getResponseHeaders();
	
	public void setResponseHeader(String header, String value);

	public InetSocketAddress getRemoteAddress();

	public HttpMethod getRequestMethod();

	public void setHttpCode(int code);

	public boolean isHeadersSent();

	public ByteBuffer getRequest();

	public void writeResponse(ByteBuffer buffer) throws IOException;

	public void setArgs(Map<String, String> args);

	public Map<String, String> getArgs();

	public String getArg(String arg);
	
	public boolean isArgSet(String arg);
}
