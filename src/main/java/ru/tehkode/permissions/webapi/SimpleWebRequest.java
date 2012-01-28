package ru.tehkode.permissions.webapi;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;

public class SimpleWebRequest implements WebRequest {

	protected HttpExchange connection;
	protected ByteBuffer readBuffer = null;
	protected int responseCode = 200;
	protected boolean headersSent = false;
	protected Map<String, String> args = new HashMap<String, String>();
	protected String basePath;
	protected String relativePath = null;

	public SimpleWebRequest(HttpExchange exchange, String basePath) {
		this.connection = exchange;
		this.basePath = basePath;

		this.fillArguments(exchange.getRequestURI());
	}

	@Override
	public ByteBuffer getRequest() {
		if (this.readBuffer == null) {
			try {
				this.readBuffer = this.readRequestData();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return this.readBuffer;
	}

	@Override
	public HttpMethod getRequestMethod() {
		return HttpMethod.fromMethod(this.connection.getRequestMethod());
	}

	@Override
	public String getBasePath() {
		return this.basePath;
	}

	@Override
	public String getRelativePath() {
		if (relativePath != null) {
			return this.relativePath;
		}

		String path = this.connection.getRequestURI().getPath();

		if (basePath != null && !basePath.isEmpty() && !basePath.equals("/")) {
			return path.substring(basePath.length());
		}

		return this.relativePath = path;
	}

	@Override
	public URI getRequestURL() {
		return this.connection.getRequestURI();
	}

	@Override
	public Map<String, List<String>> getRequestHeaders() {
		return this.getRequestHeaders();
	}

	@Override
	public Map<String, List<String>> getResponseHeaders() {
		return this.getResponseHeaders();
	}

	@Override
	public void setResponseHeader(String header, String value) {
		this.connection.getResponseHeaders().set(header, value);
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return this.connection.getRemoteAddress();
	}

	public int getHttpCode() {
		return this.responseCode;
	}

	@Override
	public void setHttpCode(int code) {
		if (this.headersSent) {
			throw new IllegalStateException("Headers already sent!");
		}

		this.responseCode = code;
	}

	@Override
	public boolean isHeadersSent() {
		return this.headersSent;
	}

	public void sendHeaders() throws IOException {
		if (!this.headersSent) {
			this.connection.sendResponseHeaders(this.responseCode, 0);
			this.headersSent = true;
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.connection.getRequestBody();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		this.sendHeaders();

		return this.connection.getResponseBody();
	}

	@Override
	public void writeResponse(ByteBuffer buffer) throws IOException {

		WritableByteChannel channel = Channels.newChannel(this.connection.getResponseBody());

		this.sendHeaders();

		channel.write(buffer);

		channel.close();
	}

	@Override
	public String getArg(String arg) {
		return this.args.get(arg);
	}

	@Override
	public Map<String, String> getArgs() {
		return this.args;
	}

	@Override
	public boolean isArgSet(String arg) {
		return this.args.containsKey(arg) && this.args.get(arg) != null;
	}

	protected void setArgs(Map<String, String> args) {
		this.args.putAll(args);
	}

	protected void fillArguments(URI request) {
		String query = request.getQuery();
		
		if (query == null) {
			return;
		}
		
		String[] params = query.split("&");

		for (String param : params) {
			if (param.contains("=")) { // ?name=value
				String nameAndValue[] = param.split("=");
				args.put(nameAndValue[0], nameAndValue[1]);
			} else { //?justname
				args.put(param, null);
			}
		}
	}

	protected ByteBuffer readRequestData() throws IOException {
		ReadableByteChannel channel = Channels.newChannel(this.connection.getRequestBody());

		ByteBuffer buffer = ByteBuffer.allocate(2048);

		while (channel.read(buffer) != -1) {
			Thread.yield(); // let dogs out

			if (!buffer.hasRemaining()) {
				// increase buffer size
				buffer.flip();
				ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + 2048);
				newBuffer.put(buffer);
				// set new buffer
				buffer = newBuffer;
			}
		}

		buffer.flip();

		return buffer;
	}
}
