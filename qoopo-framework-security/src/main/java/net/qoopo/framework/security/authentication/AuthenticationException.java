package net.qoopo.framework.security.authentication;

import net.qoopo.framework.security.exception.SecurityException;

/**
 * Abstract superclass for all exceptions related to an {@link Authentication}
 * object
 * being invalid for whatever reason.
 *
 * @author Alberto Garcia
 */
public abstract class AuthenticationException extends SecurityException {

    public AuthenticationException() {
        super();
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * root
     * cause.
     * 
     * @param msg   the detail message
     * @param cause the root cause
     */
    public AuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public AuthenticationException(String msg) {
        super(msg);
    }

}
