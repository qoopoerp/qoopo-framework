package net.qoopo.framework.security.authentication.user.check;

import net.qoopo.framework.security.authentication.password.check.CredentialsExpiredException;
import net.qoopo.framework.security.authentication.user.UserData;

/**
 * Realiza las verificaciones predeterminadas de un usuario despues de una
 * autenticacion
 */
public class DefaultPostAuthenticationChecker implements UserDataChecker {

    @Override
    public void check(UserData userData) {
        if (userData.isCredentialsExpired())
            throw new CredentialsExpiredException("Credentials expired");
    }

}
