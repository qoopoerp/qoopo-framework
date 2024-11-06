package net.qoopo.framework.security.authentication.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import net.qoopo.framework.security.authentication.password.encoder.BCryptPasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.PasswordEncoder;
import net.qoopo.framework.security.authentication.service.UserNotFoundException;
import net.qoopo.framework.security.authentication.user.DefaultUserData;
import net.qoopo.framework.security.authentication.user.UserData;

/**
 * Implementación de un repositorio de usuario que genera usuarios y password
 * aleatorias para pruebas del framework
 */
public class RandomUserRepository implements UserRepository {

    private static Logger log = Logger.getLogger("random-user-repository");

    private Map<String, UserData> users;
    private PasswordEncoder passwordEncoder;

    public RandomUserRepository() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        generate(1);
    }

    public RandomUserRepository(int counter) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        generate(counter);
    }

    public RandomUserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        generate(1);
    }

    public RandomUserRepository(int counter, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        generate(counter);
    }

    private void generate(int counter) {
        // genera usuarios aleatorios
        users = new HashMap<>();
        log.info("User generated for development only");
        IntStream.range(1, counter+1).forEach(c -> {
            String user = "user" + c;
            String password = UUID.randomUUID().toString();
            log.info("user-> [" + user + " : " + password + "]");
            users.put(user, DefaultUserData.builder()
                    .user(user)
                    .encodedPassword(passwordEncoder.encode(password))
                    .build());
        });
    }

    @Override
    public UserData findUserDataByUserName(String userName) throws UserNotFoundException {
        if (users.containsKey(userName)) {
            return users.get(userName);
        } else {
            throw new UserNotFoundException();
        }
    }

}
