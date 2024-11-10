package net.qoopo.framework.security.authorization.manager;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authorization.AuthorizationResponse;
import net.qoopo.framework.security.authorization.AuthorizationResult;

/**
 * Implementación que se encarga de validar que el usuario se encuentra
 * autenticado
 */
public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {

    @Override
    public AuthorizationResult authorize(Authentication authentication, T object) {

        boolean granted = isAuthenticated(authentication);
        return new AuthorizationResponse(granted);
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

}
