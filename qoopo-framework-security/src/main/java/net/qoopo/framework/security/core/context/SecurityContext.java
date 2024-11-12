package net.qoopo.framework.security.core.context;

import java.io.Serializable;

import net.qoopo.framework.security.authentication.Authentication;

/**
 * Define la información mímina necesaria para almacenar la información de la
 * autenticación
 */
public interface SecurityContext extends Serializable {

    /**
     * Devuele la authenticacion actual
     * 
     * @return
     */
    public Authentication getAuthentication();

    /**
     * Cambia la actual autenticación
     * @param authentication
     */
    public void setAuthentication(Authentication authentication);

}
