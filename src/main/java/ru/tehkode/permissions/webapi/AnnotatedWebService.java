/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tehkode.permissions.webapi.annotations.Path;
import ru.tehkode.permissions.webapi.exceptions.ServiceNotFoundException;

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
	public void handle(WebRequest request) {
		Map<String, String> args = null;
		
		for (MethodExecutor executor : this.methods) {
			args = executor.isMatching(request.getRelativePath());
			
			if(args == null){
				continue;
			}
			
			request.setArgs(args);
			
			System.out.println("CALLING " + args);
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

		public Map<String, String> isMatching(String uri) {			
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
	}
}
