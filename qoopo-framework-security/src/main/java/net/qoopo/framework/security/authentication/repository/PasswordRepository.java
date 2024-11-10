package net.qoopo.framework.security.authentication.repository;

import net.qoopo.framework.security.authentication.password.PasswordData;
import net.qoopo.framework.security.authentication.service.PasswordNotFoundException;
import net.qoopo.framework.security.authentication.service.UserNotFoundException;

/**
 * Repositorio del Password
 */
public interface PasswordRepository {

    /**
     * Busca un usuario por el userName
     * 
     * @param userName
     * @return
     * @throws UserNotFoundException
     */
    public PasswordData findPassword() throws PasswordNotFoundException;
}
