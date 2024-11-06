package net.qoopo.framework.security.authentication.matcher;

import net.qoopo.framework.security.authentication.Authentication;

/**
 * Siempre devuelve erdader a pesar que no exista una autenticacion
 */
public class PermitAllMatcher implements AuthenticationMatcher {

    @Override
    public boolean matches(Authentication authentication) {
        return true;
    }

}
