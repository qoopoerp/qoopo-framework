package net.qoopo.framework.security.authentication.matcher;

import net.qoopo.framework.security.authentication.Authentication;

/**
 * Siempre devuelve false a pesar que no exista una autenticacion
 */
public class DenyAllMatcher implements AuthenticationMatcher {

    @Override
    public boolean matches(Authentication authentication) {
        return false;
    }

}
