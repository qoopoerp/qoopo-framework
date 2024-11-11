package net.qoopo.framework.exception;

/**
 * Abstract superclass for all exceptions related to an {@link Authentication}
 * object
 * being invalid for whatever reason.
 *
 * @author Alberto Garcia
 */
public  class NullArgumentException extends FrameworkException {

    public NullArgumentException() {
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
    public NullArgumentException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public NullArgumentException(String msg) {
        super(msg);
    }

}
