package net.qoopo.framework.security.authentication.session;

import net.qoopo.framework.security.authentication.AuthenticationException;

public class SessionAuthenticationException extends AuthenticationException {
    public SessionAuthenticationException() {
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
    public SessionAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public SessionAuthenticationException(String msg) {
        super(msg);
    }
}
