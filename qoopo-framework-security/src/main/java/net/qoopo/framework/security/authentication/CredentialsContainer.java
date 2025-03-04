package net.qoopo.framework.security.authentication;

/**
 * Indica si la autenticacino contiene credenciales para poder eliminarlas una
 * vez fue autenticado
 */
public interface CredentialsContainer {

    /**
     * Elimina las credenciales almacenadas
     */
    public void eraseCredentials();
}
