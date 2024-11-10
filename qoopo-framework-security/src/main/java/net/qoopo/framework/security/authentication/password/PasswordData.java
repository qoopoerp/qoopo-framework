package net.qoopo.framework.security.authentication.password;

public interface PasswordData {

    public String getEncodedPassword();

    /**
     * Indica si las credenciales han expirado, Credenciales expiradas no pueden ser
     * autenticadas
     * 
     * @return
     */
    default boolean isCredentialsExpired() {
        return false;
    }

}
