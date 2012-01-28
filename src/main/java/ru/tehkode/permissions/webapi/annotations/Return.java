package ru.tehkode.permissions.webapi.annotations;

/**
 *
 * @author code
 */
public @interface Return {

	String value() default "text/plain";
}
