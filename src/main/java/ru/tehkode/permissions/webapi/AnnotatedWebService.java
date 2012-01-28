/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.io.IOException;
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
import ru.tehkode.permissions.webapi.exceptions.ServiceNotFoundException;
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
		Map<String, String> args = null;

		for (MethodExecutor executor : this.methods) {
			args = executor.matching(request.getRelativePath());

			if (args == null) {
				continue;
			}

			request.setArgs(args);

			executor.execute(this, request);
		}

		if (args == null) {
			throw new ServiceNotFoundException(request.getRequestURL().getPath());
		}
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

		public Map<String, String> matching(String uri) {
			Matcher matcher = this.matchRegexp.matcher(uri);
			if (!matcher.find()) {
				return null;
			}

			Map<String, String> argsMap = new HashMap<String, String>();

			for (int i = 0; i < this.argumentOrder.size(); i++) {
				argsMap.put(argumentOrder.get(i), matcher.group(i + 1));
			}

			return argsMap;
		}

		private Pattern prepare() {
			String regexp = method.getAnnotation(Path.class).value();
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
				r.setResponseHeader("Content-Type", mimeType);
				
				r.writeResponse(ResultRepresenter.represent(mimeType, this.method.invoke(service, r)));
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
