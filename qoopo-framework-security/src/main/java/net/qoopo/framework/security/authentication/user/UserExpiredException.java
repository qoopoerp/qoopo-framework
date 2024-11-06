package net.qoopo.framework.security.authentication.user;

import net.qoopo.framework.security.authentication.AuthenticationException;

public class UserExpiredException extends AuthenticationException {
    public UserExpiredException() {
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
    public UserExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public UserExpiredException(String msg) {
        super(msg);
    }
}
