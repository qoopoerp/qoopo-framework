package net.qoopo.framework.security.authentication.service;

import net.qoopo.framework.security.authentication.AuthenticationException;

public class PasswordNotFoundException extends AuthenticationException {

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * root
     * cause.
     * 
     * @param msg   the detail message
     * @param cause the root cause
     */
    public PasswordNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public PasswordNotFoundException(String msg) {
        super(msg);
    }

    public PasswordNotFoundException() {
        super();
    }
}
