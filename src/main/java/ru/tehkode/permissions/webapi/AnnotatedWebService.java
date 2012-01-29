/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tehkode.permissions.webapi.annotations.Path;
import ru.tehkode.permissions.webapi.annotations.Return;
import ru.tehkode.permissions.webapi.exceptions.ResourceNotFoundException;
import ru.tehkode.permissions.webapi.exceptions.WebApiException;
import ru.tehkode.permissions.webapi.representers.ResultRepresenter;

public abstract class AnnotatedWebService implements WebService {

	protected final static Pattern argumentExtractor = Pattern.compile("\\{(?:([^\\=\\/\\}]+)(?:=([^\\}]+)?)?)\\}");
	protected List<MethodExecutor> methods = new ArrayList<MethodExecutor>();

	public AnnotatedWebService() {
		this.init();
	}

	private void init() {
		for (Method method : this.getClass().getMethods()) {
			if (!method.isAnnotationPresent(Path.class)) {
				continue;
			}

			this.methods.add(new MethodExecutor(method));
		}
	}

	@Override
	public void handle(WebRequest request) throws IOException {
		if (request.getRelativePath().equals("@list")) {
			this.listMethods(request);
			return;
		}


		Map<String, List<String>> args = null;

		for (MethodExecutor executor : this.methods) {
			args = executor.matching(request.getRelativePath());

			if (args == null) { // not matching
				continue;
			}

			request.getArgs().putAll(args);

			executor.execute(this, request);
		}

		if (args == null) {
			throw new ResourceNotFoundException(request.getRequestURL().getPath());
		}
	}

	protected void listMethods(WebRequest request) throws IOException {
		Writer writer = new OutputStreamWriter(request.getOutputStream());

		writer.write("<div><h4>Available methods (" + request.getBasePath() + "):</h4><ul>");
		for (MethodExecutor executor : this.methods) {
			writer.write("<li>");
			writer.write(executor.getPath());
			writer.write("</li>");
		}
		writer.append("</ul></div>");

		writer.close();
	}

	protected class MethodExecutor {

		Pattern matchRegexp;
		Map<String, String> arguments = new HashMap<String, String>();
		List<String> argumentOrder = new ArrayList<String>();
		protected Method method;

		public MethodExecutor(Method method) {
			this.method = method;
			this.matchRegexp = this.prepare();
		}

		public Map<String, List<String>> matching(String uri) {
			Matcher matcher = this.matchRegexp.matcher(uri);
			if (!matcher.find()) {
				return null;
			}

			Map<String, List<String>> argsMap = new HashMap<String, List<String>>();

			for (int i = 0; i < this.argumentOrder.size(); i++) {
				String argument = argumentOrder.get(i);
				String value = matcher.group(i + 1);

				if (!argsMap.containsKey(argument)) {
					argsMap.put(argument, new ArrayList<String>());
				}

				argsMap.get(argument).add(value != null ? value : this.arguments.get(argument));
			}

			return argsMap;
		}

		public Method getMethod() {
			return method;
		}

		public String getPath() {
			return this.getMethod().getAnnotation(Path.class).value();
		}

		private Pattern prepare() {
			String regexp = this.getPath();
			Matcher matcher = argumentExtractor.matcher(regexp); // it won't be null, god tell me so

			int i = 0;
			while (matcher.find()) {
				boolean haveDefaultValue = matcher.group(0).contains("=");

				String argName = matcher.group(1);
				String defaultValue = matcher.group(2);

				if (defaultValue != null && defaultValue.isEmpty()) {
					defaultValue = null; //force null
				}

				if (this.arguments.containsKey(argName)) {
					throw new IllegalStateException("Argument " + argName + " is already defined!");
				}

				this.arguments.put(argName, defaultValue);
				this.argumentOrder.add(argName);

				regexp = regexp.replace(matcher.group(0), "([^\\/]+)" + (haveDefaultValue ? "?" : ""));
			}

			return Pattern.compile(regexp);
		}

		public void execute(WebService service, WebRequest r) throws IOException {
			String mimeType = "text/plain";

			if (method.isAnnotationPresent(Return.class)) {
				mimeType = method.getAnnotation(Return.class).value();
			}

			try {
				Object result = this.method.invoke(service, r);

				if (result != null) {
					r.setResponseHeader("Content-Type", mimeType);
					r.writeResponse(ResultRepresenter.represent(mimeType, result));
				}
			} catch (IOException e) {
				throw e;
			} catch (InvocationTargetException e) {
				throw new WebApiException(e.getCause());
			} catch (Throwable e) {
				throw new WebApiException(e);
			}
		}
	}
}
