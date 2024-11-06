package net.qoopo.qoopo.security.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.user.UserPasswordAutenticacion;
import net.qoopo.framework.security.authorization.AuthorizationResult;
import net.qoopo.framework.security.authorization.manager.AuthorizationManager;
import net.qoopo.framework.security.authorization.manager.RequestMatcherAuthorizationManager;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.matcher.UrlMatcher;
import net.qoopo.qoopo.security.test.request.FakeHttpServerRequest;

public class AuthorizationTest {

    private static Logger log = Logger.getLogger("AuthorizationTest-test");

    @Test
    public void urlMatches() {
        UrlMatcher matcher = new UrlMatcher("/v1/*");
        log.info("[+] urlMatches");
        log.info("[+] " + matcher.matches("/v1/getusers?id=1")); // true
        log.info("[+] " + matcher.matches("/private/")); // false
    }

    @Test
    public void authorizationTest() {
        try {
            SecurityConfig securityConfig = SecurityConfig.get();
            securityConfig
                    .enable()
                    .loginPage("/login.jsf").requestMatcher(
                            // () -> securityConfig.getRequestMatcherConfigurer()
                            config -> config
                                    .requireAuthenticatedRequest("/v1/*")
                                    .acceptRequest("/index.html")
                                    .acceptRequest("/web/*"));

            // SecurityConfig.config(securityConfig);

            AuthorizationManager<HttpServletRequest> authorizationManager = new RequestMatcherAuthorizationManager(
                    SecurityConfig.get().getRequestMatcherConfigurer());

            Authentication authenticated = new UserPasswordAutenticacion("user", "password");
            authenticated.setAuthenticated(true);

            Authentication notAuthenticated = new UserPasswordAutenticacion("user2", "password2");
            notAuthenticated.setAuthenticated(false);

            HttpServletRequest request = new FakeHttpServerRequest("/paginanueva");
            AuthorizationResult authorizationResult = authorizationManager.authorize(() -> authenticated, request);
            assertFalse(authorizationResult.isGranted());
            authorizationResult = authorizationManager.authorize(() -> notAuthenticated, request);
            assertFalse(authorizationResult.isGranted());

            request = new FakeHttpServerRequest("/v1/getcontact/85");
            authorizationResult = authorizationManager.authorize(() -> authenticated, request);
            assertTrue(authorizationResult.isGranted());
            authorizationResult = authorizationManager.authorize(() -> notAuthenticated, request);
            assertFalse(authorizationResult.isGranted());

            request = new FakeHttpServerRequest("/web/nuvapagina");
            authorizationResult = authorizationManager.authorize(() -> authenticated, request);
            assertTrue(authorizationResult.isGranted());
            authorizationResult = authorizationManager.authorize(() -> notAuthenticated, request);
            assertTrue(authorizationResult.isGranted());

            request = FakeHttpServerRequest.builder().servletPath("/login.jsf").method("POST").build();

            authorizationResult = authorizationManager.authorize(() -> authenticated, request);
            assertTrue(authorizationResult.isGranted());
            authorizationResult = authorizationManager.authorize(() -> notAuthenticated, request);
            assertTrue(authorizationResult.isGranted());

            request = FakeHttpServerRequest.builder().servletPath("/login.jsf")
                    .queryString("error=AccessDenied")
                    .method("POST").build();

            authorizationResult = authorizationManager.authorize(() -> authenticated, request);
            assertTrue(authorizationResult.isGranted());
            authorizationResult = authorizationManager.authorize(() -> notAuthenticated, request);
            assertTrue(authorizationResult.isGranted());

        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

}
