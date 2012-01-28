package ru.tehkode.permissions.webapi.representers;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public abstract class ResultRepresenter {

	private final static Map<String, Representer> representers = new HashMap<String, Representer>();

	static {
		representers.put("text/plain", new TextRepresenter());
		representers.put("text/html", new TextRepresenter());
		representers.put("application/json", new JsonRepresenter());
	}

	public static ByteBuffer represent(String mimeType, Object response) {
		if (!representers.containsKey(mimeType)) { // failback to mime/plain
			mimeType = "text/plain";
		}

		return representers.get(mimeType).represent(mimeType, response);
	}
}
