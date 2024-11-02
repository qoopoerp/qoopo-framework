package net.qoopo.framework.security.authentication.manager;

import net.qoopo.framework.security.authentication.AuthenticationException;

public class NoProviderException extends AuthenticationException {
    public NoProviderException() {
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
    public NoProviderException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public NoProviderException(String msg) {
        super(msg);
    }
}
