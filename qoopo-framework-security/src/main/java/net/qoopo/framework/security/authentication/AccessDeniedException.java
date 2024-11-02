package net.qoopo.framework.security.authentication;

public class AccessDeniedException extends AuthenticationException {
    public AccessDeniedException() {
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
    public AccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public AccessDeniedException(String msg) {
        super(msg);
    }
}
