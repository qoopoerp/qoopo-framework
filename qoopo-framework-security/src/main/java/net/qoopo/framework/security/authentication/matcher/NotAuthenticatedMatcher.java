package net.qoopo.framework.security.authentication.matcher;

import net.qoopo.framework.security.authentication.Authentication;

/**
 * Valida que no exista una autenticacion
 */
public class NotAuthenticatedMatcher implements AuthenticationMatcher {

    @Override
    public boolean matches(Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated();
    }

}
