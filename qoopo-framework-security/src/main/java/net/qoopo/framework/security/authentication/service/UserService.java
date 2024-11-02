package net.qoopo.framework.security.authentication.service;

import net.qoopo.framework.security.authentication.password.encoder.PasswordEncoder;
import net.qoopo.framework.security.authentication.user.UserData;

/**
 * Servicio que se encarga de buscar un usuario para su posterior valiadación de
 * credenciales
 */
public interface UserService {
    /**
     * Busca un usuario por el username
     * 
     * @param username
     * @return
     * @throws UserNotFoundException
     */
    public UserData findUserDataByUserName(String username) throws UserNotFoundException;

    /**
     * Devuelve el codificador de password asociado al servicio
     * 
     * @return
     */
    public PasswordEncoder getPasswordEncoder();
}
