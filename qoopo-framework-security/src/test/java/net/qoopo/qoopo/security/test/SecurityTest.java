package net.qoopo.qoopo.security.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.SecurityConfigRouter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.password.DefaultUserPasswordAutenticationProvider;
import net.qoopo.framework.security.authentication.password.UserPasswordAutenticacion;
import net.qoopo.framework.security.authentication.password.encoder.Argon2PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.BCryptPasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.DefaulPasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.Md5PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.Sha256PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.Sha512PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.ShaPasswordEncoder;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.repository.DefaultUserRepository;
import net.qoopo.framework.security.authentication.repository.RandomUserRepository;
import net.qoopo.framework.security.authentication.repository.UserRepository;
import net.qoopo.framework.security.authentication.service.DefaultUserService;
import net.qoopo.framework.security.authentication.service.UserService;
import net.qoopo.framework.security.authentication.user.DefaultUserData;
import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.matcher.RequestMatcher;
import net.qoopo.framework.security.matcher.RoutesDontRequiresAuthenticacionMatcher;
import net.qoopo.framework.security.matcher.RoutesRequiresAutenticationMatcher;
import net.qoopo.framework.security.router.SecurityRoute;
import net.qoopo.framework.security.router.SecurityRouter;
import net.qoopo.qoopo.security.test.request.FakeHttpServerRequest;

public class SecurityTest {

    private static Logger log = Logger.getLogger("security-test");

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {

    }

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void passwordsEncoders() {
        try {
            String password = "superpassword";
            String encodedPassword;

            log.info("password=" + password);
            PasswordEncoder passwordEncoder = new DefaulPasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("default->[" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));

            passwordEncoder = new Md5PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("md5->[" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue("d1e576b71ccef5978d221fadf4f0e289".equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new ShaPasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("SHA->[" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue("77be4fc97f77f5f48308942bb6e32aacabed9cef".equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new Sha256PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("sha256->[" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue("ba21767ae494afe5a2165dcb3338c5323e9907050e34542c405d575cc31bf527"
                    .equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new Sha512PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("sha512->[" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue(
                    "111fca2d52def4c33f4d8f1be7e74d14b65d365e5ddb91610c3c0dbecc192073b0b0df28213e3828cc0321f6286baf94449a4f8803203be3293595f4d67ff7e2"
                            .equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new BCryptPasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("Bcrypt->[" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));

            passwordEncoder = new Argon2PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("Argon2->[" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));

            passwordEncoder = new BCryptPasswordEncoder();
            encodedPassword = passwordEncoder.encode("recoveredpassword");
            log.info("recoveredpassword->[" + encodedPassword + "]");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void usersGeneration() {
        try {
            repository = new RandomUserRepository(passwordEncoder);
            UserData userData = repository.findUserDataByUserName("admin");
            assertTrue(userData != null);
            log.info("user recovered -> " + userData.getUser() + " : " + userData.getEncodedPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void loginControllerFailsWithRandomRepository() {
        try {
            repository = new RandomUserRepository(passwordEncoder);
            UserData userData = repository.findUserDataByUserName("admin");
            UserService userService = new DefaultUserService(repository, passwordEncoder);
            AuthenticationProvider authenticationProvider = new DefaultUserPasswordAutenticationProvider(
                    userService);
            Authentication authentication = new UserPasswordAutenticacion("admin", "admin");

            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }

            assertFalse(authentication.isAuthenticated());
            authentication = new UserPasswordAutenticacion(userData.getUser(), userData.getEncodedPassword());
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
    public void loginControllerSuccessWithDeatultRepository() {
        try {

            String user = "admin";
            String password = "adminpassword";

            String user2 = "user";
            String password2 = "userpassword";

            repository = new DefaultUserRepository(List.of(
                    DefaultUserData.builder().user(user)
                            .encodedPassword(passwordEncoder.encode(password)).build(),
                    DefaultUserData.builder().user(user2)
                            .encodedPassword(passwordEncoder.encode(password2)).build()

            ));

            UserData userData = repository.findUserDataByUserName("admin");
            assertTrue(userData != null);
            UserService userService = new DefaultUserService(repository, passwordEncoder);
            AuthenticationProvider authenticationProvider = new DefaultUserPasswordAutenticationProvider(
                    userService);

            Authentication authentication = new UserPasswordAutenticacion("admin", "admin");
            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertFalse(authentication.isAuthenticated());

            authentication = new UserPasswordAutenticacion(user, password);
            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertTrue(authentication.isAuthenticated());

            authentication = new UserPasswordAutenticacion(user2, password2);
            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertTrue(authentication.isAuthenticated());

            authentication = new UserPasswordAutenticacion(user, password2);
            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertFalse(authentication.isAuthenticated());

            authentication = new UserPasswordAutenticacion(user2, password);
            try {
                authenticationProvider.authenticate(authentication);
            } catch (Exception e) {

            }
            assertFalse(authentication.isAuthenticated());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testRoutes() {
        try {

            SecurityConfigRouter configRouter = SecurityConfigRouter.get();
            configRouter.configRoute("/v1/*").requireAuthenticated();
            configRouter.configRoute("/index.html").permitAll();
            configRouter.configRoute("/web/*").permitAll();
            configRouter.config();

            SecurityRoute route = SecurityRouter.validateRoute("/paginanueva");
            assertTrue(route.getType() == SecurityRoute.TYPE_INVALID);

            route = SecurityRouter.validateRoute("/index.html");
            assertTrue(route.getType() == SecurityRoute.TYPE_PUBLIC);

            route = SecurityRouter.validateRoute("/v1/api/getUsers?id=85");
            log.info(route.toString());
            assertTrue(route.getType() == SecurityRoute.TYPE_BACKEND);

        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    public void testRoutesMatches() {
        try {

            SecurityConfigRouter configRouter = SecurityConfigRouter.get();
            configRouter.configRoute("/v1/*").requireAuthenticated();
            configRouter.configRoute("/index.html").permitAll();
            configRouter.configRoute("/web/*").permitAll();
            configRouter.config();

            RequestMatcher requiresAuthenticationMatcher = new RoutesRequiresAutenticationMatcher();

            RequestMatcher dontRequiresAuthenticationRequestMatcher = new RoutesDontRequiresAuthenticacionMatcher();

            HttpServletRequest request = new FakeHttpServerRequest("/paginanueva");
            assertFalse(requiresAuthenticationMatcher.match(request));
            assertFalse(dontRequiresAuthenticationRequestMatcher.match(request));

            request = new FakeHttpServerRequest("/v1/getcontact/85");
            assertTrue(requiresAuthenticationMatcher.match(request));
            assertFalse(dontRequiresAuthenticationRequestMatcher.match(request));

            request = new FakeHttpServerRequest("/web/nuvapagina");
            assertFalse(requiresAuthenticationMatcher.match(request));
            assertTrue(dontRequiresAuthenticationRequestMatcher.match(request));

        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }
}
