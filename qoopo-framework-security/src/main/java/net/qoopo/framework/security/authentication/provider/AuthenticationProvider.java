package net.qoopo.framework.security.authentication.provider;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;

/**
 * Represneta una implementación de un provedor específico de autenticación
 * 
 * ejemplo: Autenticación por usuario y contraseña
 * ejemplo: Autenticación por JWT
 */
public interface AuthenticationProvider {

    public Authentication authenticate(Authentication authentication) throws AuthenticationException;

    public boolean supports(Class<?> authentication);
}
