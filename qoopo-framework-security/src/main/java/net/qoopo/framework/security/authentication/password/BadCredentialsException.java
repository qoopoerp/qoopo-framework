package net.qoopo.framework.security.authentication.password;

import net.qoopo.framework.security.authentication.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {
    public BadCredentialsException() {
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
    public BadCredentialsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public BadCredentialsException(String msg) {
        super(msg);
    }
}
