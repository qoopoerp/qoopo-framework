package net.qoopo.framework.security.authentication.user.check;

import net.qoopo.framework.security.authentication.AuthenticationException;

public class UserLockedException extends AuthenticationException {
    public UserLockedException() {
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
    public UserLockedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and
     * no
     * root cause.
     * 
     * @param msg the detail message
     */
    public UserLockedException(String msg) {
        super(msg);
    }
}
