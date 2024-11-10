package net.qoopo.qoopo.security.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.password.encoder.BCryptPasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.PasswordEncoder;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.repository.InMemoryUserRepository;
import net.qoopo.framework.security.authentication.repository.RandomUserRepository;
import net.qoopo.framework.security.authentication.repository.UserRepository;
import net.qoopo.framework.security.authentication.service.DefaultUserService;
import net.qoopo.framework.security.authentication.service.UserService;
import net.qoopo.framework.security.authentication.user.DefaultUserData;
import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.authentication.user.UserPasswordAutenticacion;
import net.qoopo.framework.security.authentication.user.provider.UserPasswordAutenticationProvider;

public class AuthenticationTest {

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
    public void failsWithRandomRepository() {
        try {
            repository = new RandomUserRepository(passwordEncoder);
            UserData userData = repository.findUserDataByUserName("user1");
            UserService userService = new DefaultUserService(repository, passwordEncoder);
            AuthenticationProvider authenticationProvider = new UserPasswordAutenticationProvider(
                    userService);
            Authentication authentication = UserPasswordAutenticacion.unauthenticated("user1", "admin");

            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }

            assertFalse(authentication.isAuthenticated());
            authentication = UserPasswordAutenticacion.unauthenticated(userData.getUser(),
                    userData.getEncodedPassword());
            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertFalse(authentication.isAuthenticated());
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void successWithDeatultRepository() {
        try {

            String user = "admin";
            String password = "adminpassword";

            String user2 = "user";
            String password2 = "userpassword";

            repository = new InMemoryUserRepository(List.of(
                    DefaultUserData.builder().user(user)
                            .encodedPassword(passwordEncoder.encode(password)).build(),
                    DefaultUserData.builder().user(user2)
                            .encodedPassword(passwordEncoder.encode(password2)).build()));

            UserData userData = repository.findUserDataByUserName("admin");
            assertTrue(userData != null);
            UserService userService = new DefaultUserService(repository, passwordEncoder);
            AuthenticationProvider authenticationProvider = new UserPasswordAutenticationProvider(
                    userService);

            Authentication authentication = UserPasswordAutenticacion.unauthenticated("admin", "admin");
            try {
                authentication = authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertFalse(authentication.isAuthenticated());

            authentication = UserPasswordAutenticacion.unauthenticated(user, password);
            authentication = authenticationProvider.authenticate(authentication);
            log.info(authentication.toString());
            assertTrue(authentication.isAuthenticated());

            authentication = UserPasswordAutenticacion.unauthenticated(user2, password2);
            try {
                authentication = authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertTrue(authentication.isAuthenticated());

            authentication = UserPasswordAutenticacion.unauthenticated(user, password2);
            try {
                authentication = authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertFalse(authentication.isAuthenticated());

            authentication = UserPasswordAutenticacion.unauthenticated(user2, password);
            try {
                authentication = authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertFalse(authentication.isAuthenticated());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
