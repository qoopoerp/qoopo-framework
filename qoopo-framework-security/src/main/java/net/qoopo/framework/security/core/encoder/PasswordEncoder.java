package net.qoopo.framework.security.core.encoder;

public interface PasswordEncoder {

    /**
     * Codifica la password en claro,
     * 
     * @param password
     * @return
     */
    public String encode(String password);

    /**
     * Verifica que la password codificada almacenada corresponde a un password en
     * claro
     * 
     * @param clearPassword   Password en claro para codificar y comparar
     * @param encodedPassword password codificada almacenada
     * @return
     */
    public boolean validate(String clearPassword, String encodedPassword);

}
