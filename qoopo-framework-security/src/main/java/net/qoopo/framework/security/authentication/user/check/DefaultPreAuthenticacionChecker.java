package net.qoopo.framework.security.authentication.user.check;

import net.qoopo.framework.security.authentication.user.UserData;

/**
 * Realiza comprobaciones estandar antes de una authenticacion
 */
public class DefaultPreAuthenticacionChecker implements UserDataChecker {

    @Override
    public void check(UserData userData) {
        if (userData.isAccountLocked())
            throw new UserLockedException("User locked");

        if (userData.isAccountExpired())
            throw new UserExpiredException("User expired");

        if (!userData.isEnabled())
            throw new UserDisabledException("User disabled");
    }

}
