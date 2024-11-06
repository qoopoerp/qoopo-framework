package net.qoopo.framework.security.authorization;

public class AuthorizationDeniedException extends AuthorizationException {
    public AuthorizationDeniedException() {
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
    public AuthorizationDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public AuthorizationDeniedException(String msg) {
        super(msg);
    }
}
