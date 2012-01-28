/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.tehkode.permissions.webapi.representers;

import java.nio.ByteBuffer;


public abstract class StringRepresenter implements Representer{

	public abstract String representToString(String mimeType, Object obj);
	
	@Override
	public ByteBuffer represent(String mimeType, Object response) {
		return ByteBuffer.wrap(this.representToString(mimeType, response).getBytes());
	}

	
	
}
