package net.qoopo.framework.security.authorization;

import lombok.AllArgsConstructor;

/**
 * Implementación predeterminada de una respuesta de un resultado de validación
 * de autorizacion
 */
@AllArgsConstructor
public class AuthorizationResponse implements AuthorizationResult {

    private boolean granted;

    @Override
    public boolean isGranted() {
        return granted;
    }

}
