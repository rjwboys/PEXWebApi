package ru.tehkode.permissions.webapi.representers;

import org.json.simple.JSONAware;
import org.json.simple.JSONValue;

public class JsonRepresenter extends TextRepresenter {

	@Override
	public String representToString(String mimeType, Object obj) {
		if (obj instanceof JSONAware) {
			return ((JSONAware) obj).toJSONString();
		}

		return JSONValue.toJSONString(obj);
	}
}
