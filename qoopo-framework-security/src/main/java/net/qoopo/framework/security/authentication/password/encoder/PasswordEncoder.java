package net.qoopo.framework.security.authentication.password.encoder;

public interface PasswordEncoder {

    /**
     * Codifica la password en claro, 
     * @param password
     * @return
     */
    public String encode(String password);

    /**
     * Verifica que la password codificada almacenada corresponde a un password en claro 
     * @param clearPassword Password en claro para codificar y comparar
     * @param encodedPassword password codiicada almacenada
     * @return
     */
    public boolean validate(String clearPassword, String encodedPassword);

    // public String getPasswordEncoded();
}