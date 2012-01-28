package ru.tehkode.permissions.webapi.auth;

import ru.tehkode.permissions.webapi.WebRequest;


public interface WebAutheniticator {

	public boolean autheniticate(WebRequest request);
}
