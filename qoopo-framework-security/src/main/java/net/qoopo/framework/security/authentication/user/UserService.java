package net.qoopo.framework.security.authentication.user;

import net.qoopo.framework.security.core.encoder.PasswordEncoder;

/**
 * Servicio que se encarga de buscar un usuario para su posterior validación de
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
