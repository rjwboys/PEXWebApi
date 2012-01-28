package ru.tehkode.permissions.webapi.auth;

import ru.tehkode.permissions.webapi.WebRequest;
import ru.tehkode.permissions.webapi.WebService;


public interface WebAutheniticator {

	public boolean autheniticate(WebService service, WebRequest request);
}
