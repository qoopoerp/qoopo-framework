package net.qoopo.framework.security.authentication.manager;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;

/**
 * Representa una implementación de un manejador de Autehtnicación
 * 
 * Su función es manejar al provedor de Autenticación <<AuthenticationProvider>>
 */
public interface AuthenticationManager {
    public Authentication authenticate(Authentication authentication) throws AuthenticationException;

}
