/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import javax.management.RuntimeErrorException;
import ru.tehkode.permissions.webapi.exceptions.ResourceNotFoundException;
import ru.tehkode.permissions.webapi.exceptions.WebApiException;

public class SimpleWebServiceManager implements WebServiceManager, HttpHandler {

	protected final static Logger logger = Logger.getLogger("Minecraft");
	protected HttpServer server;
	protected Map<String, WebService> services = new HashMap<String, WebService>();
	protected boolean running = false;

	public SimpleWebServiceManager(int port) {
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.setExecutor(Executors.newCachedThreadPool());

			server.createContext("/", this);
		} catch (Throwable e) {
			throw new IllegalStateException(e);
		}

		logger.info("[WebApi] WebServer init!");
	}

	@Override
	public void start() {
		logger.info("[WebApi] WebServer started!");
		server.start();
		this.running = true;
	}

	@Override
	public void stop() {
		logger.info("[WebApi] WebServer stopped!");
		server.stop(0);
		this.running = false;
	}

	public boolean isRunning() {
		return this.running;
	}

	@Override
	public Map<String, WebService> getServices() {
		return this.services;
	}

	public WebService getService(String basePath) {
		return this.services.get(basePath);
	}

	@Override
	public void registerService(String basePath, WebService service) {
		if (basePath == null || basePath.isEmpty()) {
			throw new IllegalArgumentException("Wrong base path");
		}

		if (basePath.endsWith("/") && basePath.length() > 1) { // cut off trailing slash
			basePath = basePath.substring(0, basePath.length() - 1);
		}

		services.put(basePath, service);
	}

	@Override
	public void unregisterService(WebService service) {
		services.values().remove(service);
	}

	@Override
	public void handle(HttpExchange ex) throws IOException {
		try {
			String basePath = this.lookupURI(ex.getRequestURI());
			if (basePath == null) { // service not found
				throw new ResourceNotFoundException(ex.getRequestURI().toString());
			}
			
			WebService service = this.getService(basePath);
			SimpleWebRequest request = new SimpleWebRequest(ex, basePath);
			
			service.handle(request);

			if (!request.isHeadersSent()) {
				ex.sendResponseHeaders(request.getHttpCode(), 0);
			}
		} catch (IOException e) {
			throw e; // forward IOException
		} catch (WebApiException e) {
			this.handleError(e, ex);
		} catch (Throwable e) { // handle other exceptions
			this.handleError(new WebApiException(e), ex);
		} finally {
			if (ex != null) {
				ex.close();
			}
		}
	}

	protected void handleError(WebApiException e, HttpExchange ex) throws IOException {
		ex.getResponseHeaders().set("Content-Type", "text/html");
		ex.sendResponseHeaders(e.getStatusCode(), 0);
		PrintWriter writer = new PrintWriter(ex.getResponseBody());
		writer.print("<html>"
				+ "<head><title>" + e.getStatusCode() + " " + e.getMessage() + "</title></head>"
				+ "<body><h1>" + e.getStatusCode() + " " + e.getMessage() + "</h1><hr/>");
		if (e instanceof ResourceNotFoundException) {
			writer.print("<strong>Sorry, but specified page is not found. Check URL for typos.</strong>");
		} else {
			writer.print("<strong>Error occured during processing request:</strong>"
					+ "<div><pre style=\"background: #cfcfcf;margin: 0.5em;padding: 1.5em;\">");
			if (e.getCause() != null && e.getCause() != e) {
				e.getCause().printStackTrace(writer);
			} else {
				e.printStackTrace(writer);
			}
			writer.print("</pre></div>");
		}

		writer.print("<hr/><span style=\"font-size: 8pt;color: #ccc;\">PEXWebApi 1.18</span></body></html>"); // @todo aquire plugin information programmatically
		writer.close();
	}

	public String lookupURI(URI uri) {
		String path = null;
		int lastLength = 0;

		for (String servicePath : this.services.keySet()) { // sort-o-choose
			if (servicePath.length() > lastLength && uri.getPath().startsWith(servicePath)) {
				path = servicePath;
				lastLength = path.length();
			}
		}

		return path;
	}
}
