package net.qoopo.framework.security.authentication.password;

import net.qoopo.framework.security.authentication.AuthenticationException;

public class CredentialsExpiredException extends AuthenticationException {
    public CredentialsExpiredException() {
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
    public CredentialsExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public CredentialsExpiredException(String msg) {
        super(msg);
    }
}
