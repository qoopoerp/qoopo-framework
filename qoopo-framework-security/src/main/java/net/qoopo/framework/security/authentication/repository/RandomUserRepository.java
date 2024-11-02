package net.qoopo.framework.security.authentication.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import net.qoopo.framework.random.RandomStringsGenerator;
import net.qoopo.framework.security.authentication.password.encoder.DefaulPasswordEncoder;
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
        this.passwordEncoder = new DefaulPasswordEncoder();
        generate();
    }

    public RandomUserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        generate();
    }

    private void generate() {
        // genera usuarios aleatorios
        users = new HashMap<>();

        String password = RandomStringsGenerator.generateAlphanumeric(12);
        log.info("user-> admin:" + password);
        users.put("admin", DefaultUserData.builder().user("admin")
                .encodedPassword(passwordEncoder.encode(password)).build());

        IntStream.range(0, 5).forEach(c -> {
            String user = "admin" + c;
            String password2 = RandomStringsGenerator.generateAlphanumeric(12);

            log.info("user-> " + user + ":" + password2);
            users.put(user, DefaultUserData.builder().user(user)
                    .encodedPassword(passwordEncoder.encode(password2)).build());
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
