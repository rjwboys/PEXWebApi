/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	public InputStream getInputStream() throws IOException;

	public OutputStream getOutputStream() throws IOException;

	public void writeResponse(ByteBuffer buffer) throws IOException;

	public Map<String, List<String>> getArgs();

	public String getArg(String arg);
	
	public List<String> getArgList(String arg);

	public boolean isArgSet(String arg);
}
