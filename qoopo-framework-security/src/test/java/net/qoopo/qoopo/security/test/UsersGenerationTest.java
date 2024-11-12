package net.qoopo.qoopo.security.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qoopo.framework.security.authentication.repository.RandomUserRepository;
import net.qoopo.framework.security.authentication.repository.UserRepository;
import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.core.encoder.BCryptPasswordEncoder;
import net.qoopo.framework.security.core.encoder.PasswordEncoder;

public class UsersGenerationTest {

    private static Logger log = Logger.getLogger("security-test");

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void usersGeneration() {
        try {
            repository = new RandomUserRepository(passwordEncoder);
            UserData userData = repository.findUserDataByUserName("user1");
            assertTrue(userData != null);
            log.info("user recovered -> " + userData.getUser() + " : " + userData.getEncodedPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

}
