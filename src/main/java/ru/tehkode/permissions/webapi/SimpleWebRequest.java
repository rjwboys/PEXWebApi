package ru.tehkode.permissions.webapi;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.Map;

public class SimpleWebRequest implements WebRequest {

	protected HttpExchange connection;
	protected ByteBuffer readBuffer = null;
	protected int responseCode = 200;
	protected boolean headersSent = false;
	protected Map<String, String> args = null;
	protected String basePath;
	protected String relativePath = null;

	public SimpleWebRequest(HttpExchange exchange, String basePath) {
		this.connection = exchange;
		this.basePath = basePath;
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

	@Override
	public void writeResponse(ByteBuffer buffer) {
		try {
			WritableByteChannel channel = Channels.newChannel(this.connection.getResponseBody());

			if (!this.headersSent) {
				this.connection.sendResponseHeaders(this.responseCode, 0);
				this.headersSent = true;
			}

			channel.write(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
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

	@Override
	public String getArg(String arg) {
		return this.args.get(arg);
	}

	@Override
	public Map<String, String> getArgs() {
		return this.args;
	}

	@Override
	public void setArgs(Map<String, String> args) {
		this.args = args;
	}

	@Override
	public boolean isArgSet(String arg) {
		return this.args.containsKey(arg) && this.args.get(arg) != null;
	}
}
