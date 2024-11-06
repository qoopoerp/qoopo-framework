package net.qoopo.framework.security.authorization;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.exception.SecurityException;

/**
 * Abstract superclass for all exceptions related to an {@link Authentication}
 * object
 * being invalid for whatever reason.
 *
 * @author Alberto Garcia
 */
public abstract class AuthorizationException extends SecurityException {

    public AuthorizationException() {
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
    public AuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public AuthorizationException(String msg) {
        super(msg);
    }



}
