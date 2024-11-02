package net.qoopo.framework.security.authentication.service;

import net.qoopo.framework.security.authentication.password.encoder.PasswordEncoder;
import net.qoopo.framework.security.authentication.user.PasswordData;

/**
 * Servicio que se encarga de obtener el password almacenado
 */
public interface PasswordService {
   /**
    * Carga la password almacenada
    * @return
    * @throws PasswordNotFoundException
    */
    public PasswordData findPassword() throws PasswordNotFoundException;

    /**
     * Devuelve el codificador de password asociado al servicio
     * 
     * @return
     */
    public PasswordEncoder getPasswordEncoder();
}
