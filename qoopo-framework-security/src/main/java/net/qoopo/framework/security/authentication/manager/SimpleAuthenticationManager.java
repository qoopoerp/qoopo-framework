package net.qoopo.framework.security.authentication.manager;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;

/**
 * Implementación prederminada del Gestor de Autenticaciones donde mane un solo proveedor de autenticacion
 */
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAuthenticationManager implements AuthenticationManager {

    private AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationProvider.authenticate(authentication);
    }

}
