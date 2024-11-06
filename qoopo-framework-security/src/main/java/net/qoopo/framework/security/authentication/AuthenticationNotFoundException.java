package net.qoopo.framework.security.authentication;

public class AuthenticationNotFoundException extends AuthenticationException {
    public AuthenticationNotFoundException() {
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
    public AuthenticationNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public AuthenticationNotFoundException(String msg) {
        super(msg);
    }
}
