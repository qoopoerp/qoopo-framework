package net.qoopo.framework.security.authentication.user.check;

import net.qoopo.framework.security.authentication.user.UserData;

/**
 * Realiza validaciones a un usuario
 */
public interface UserDataChecker {
    public void check(UserData userData);

}
