package ru.tehkode.permissions.webapi.representers;

import java.nio.ByteBuffer;

public interface Representer {

	public abstract ByteBuffer represent(String mimeType, Object response);
}
