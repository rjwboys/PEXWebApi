package ru.tehkode.permissions.webapi;

public @interface Method {
	HttpMethod value() default HttpMethod.GET;
}
