package net.qoopo.framework.security.authentication.user;

import net.qoopo.framework.security.authentication.repository.UserRepository;
import net.qoopo.framework.security.core.encoder.PasswordEncoder;

/**
 * Implementación predeterminada de UserService utilizada en el framework
 */
public class DefaultUserService implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserData findUserDataByUserName(String user) throws UserNotFoundException {
        return userRepository.findUserDataByUserName(user);
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

}
