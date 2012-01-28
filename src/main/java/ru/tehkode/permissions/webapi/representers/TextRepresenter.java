package ru.tehkode.permissions.webapi.representers;

public class TextRepresenter extends StringRepresenter {

	@Override
	public String representToString(String mimeType, Object obj) {
		System.out.println("REPRESENTING " + obj.toString());
	
		return obj.toString();
	}
}
