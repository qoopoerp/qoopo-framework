package net.qoopo.framework.security.authentication.matcher;

import net.qoopo.framework.security.authentication.Authentication;

/**
 * Requiere una autenticacion existente
 */
public class AuthenticacionRequiredMatcher implements AuthenticationMatcher {

    @Override
    public boolean matches(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

}
