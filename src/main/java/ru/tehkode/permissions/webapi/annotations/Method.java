/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.permissions.webapi.annotations;

import ru.tehkode.permissions.webapi.HttpMethod;

/**
 *
 * @author code
 */
public @interface Method {
	HttpMethod value() default HttpMethod.GET;
}
