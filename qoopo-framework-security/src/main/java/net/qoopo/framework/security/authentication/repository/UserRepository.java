package net.qoopo.framework.security.authentication.repository;

import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.authentication.user.UserNotFoundException;

/**
 * Repositorio del Usuario
 */
public interface UserRepository {

    /**
     * Busca un usuario por el userName
     * 
     * @param userName
     * @return
     * @throws UserNotFoundException
     */
    public UserData findUserDataByUserName(String userName) throws UserNotFoundException;
}
