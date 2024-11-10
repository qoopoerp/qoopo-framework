package net.qoopo.qoopo.security.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
import net.qoopo.framework.security.matcher.util.IPValidator;
import net.qoopo.framework.security.permission.DefaultGrantedPermission;
import net.qoopo.framework.security.permission.GrantedPermission;
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
        public void validateIps() {
                assertTrue(IPValidator.isValidIPv4("200.107.34.226"));
                assertFalse(IPValidator.isValidIPv4("256.256.256.256"));
                assertTrue(IPValidator.isValidIPv6("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
                assertTrue(IPValidator.isValidIPv6("1234:5678:9ABC:DEF0:1234:5678:9ABC:DEF0"));
        }

        @Test
        public void authorizationRoles() {
                SecurityConfig.get()
                                .enable()
                                .login(login -> login.loginPage("/login.jsf"))
                                .authorizeRequests(config -> config
                                                .hasAnyRole(List.of("/private/admin/*",
                                                                "/private/manage*"), "ADMIN",
                                                                "MANAGEMENT")
                                                .hasRole(List.of("/monitor"), "ADMIN")

                                );

                AuthorizationManager<HttpServletRequest> authorizationManager = new RequestMatcherAuthorizationManager(
                                SecurityConfig.get().getRequestMatcherConfigurer());
                Authentication authenticated = UserPasswordAutenticacion.authenticated("user", "",
                                DefaultGrantedPermission.of("ADMIN", "USER"));

                Authentication authenticated2 = UserPasswordAutenticacion.authenticated("user2", "",
                                DefaultGrantedPermission.of("MANAGEMENT", "USER"));
                Authentication notAuthenticated = UserPasswordAutenticacion.unauthenticated("user2", "password2");

                // ips
                HttpServletRequest request = FakeHttpServerRequest.builder()
                                .servletPath("/private/admin/users")
                                .build();

                AuthorizationResult authorizationResult = authorizationManager.authorize(authenticated, request);
                assertTrue(authorizationResult.isGranted());

                authorizationResult = authorizationManager.authorize(authenticated2, request);
                assertTrue(authorizationResult.isGranted());

                authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                assertFalse(authorizationResult.isGranted());

                request = FakeHttpServerRequest.builder()
                                .servletPath("/private/manage/credentials")
                                .build();
                authorizationResult = authorizationManager.authorize(authenticated, request);
                assertTrue(authorizationResult.isGranted());

                authorizationResult = authorizationManager.authorize(authenticated2, request);
                assertTrue(authorizationResult.isGranted());

                authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                assertFalse(authorizationResult.isGranted());

                request = FakeHttpServerRequest.builder()
                                .servletPath("/monitor")
                                .build();
                authorizationResult = authorizationManager.authorize(authenticated, request);
                assertTrue(authorizationResult.isGranted());

                authorizationResult = authorizationManager.authorize(authenticated2, request);
                assertFalse(authorizationResult.isGranted());

                authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                assertFalse(authorizationResult.isGranted());

        }

        @Test
        public void authorizationIp() {
                SecurityConfig.get()
                                .enable()
                                .login(login -> login.loginPage("/login.jsf"))
                                .authorizeRequests(config -> config
                                                .permit("/index.html")
                                                .authenticatedIp("10.10.10.6")
                                                .permitIp("10.10.10.8")
                                                .denyIp("10.10.10.7"));

                AuthorizationManager<HttpServletRequest> authorizationManager = new RequestMatcherAuthorizationManager(
                                SecurityConfig.get().getRequestMatcherConfigurer());
                Authentication authenticated = UserPasswordAutenticacion.authenticated("user", "password",
                                GrantedPermission.empty);
                Authentication notAuthenticated = UserPasswordAutenticacion.unauthenticated("user2", "password2");

                // ips
                HttpServletRequest request = FakeHttpServerRequest.builder()
                                .servletPath("/asdasd")
                                .remoteAddr("10.10.10.8")
                                .build();

                AuthorizationResult authorizationResult = authorizationManager.authorize(authenticated, request);
                assertTrue(authorizationResult.isGranted());
                authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                assertTrue(authorizationResult.isGranted());

                request = FakeHttpServerRequest.builder()
                                .servletPath("/asdasd")
                                .remoteAddr("10.10.10.7")
                                .build();

                authorizationResult = authorizationManager.authorize(authenticated, request);
                assertFalse(authorizationResult.isGranted());
                authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                assertFalse(authorizationResult.isGranted());

                request = FakeHttpServerRequest.builder()
                                .servletPath("/asdasd")
                                .remoteAddr("10.10.10.6")
                                .build();

                authorizationResult = authorizationManager.authorize(authenticated, request);
                assertTrue(authorizationResult.isGranted());
                authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                assertFalse(authorizationResult.isGranted());
        }

        @Test
        public void authorizationTest() {
                try {
                        SecurityConfig.get()
                                        .enable()
                                        .login(login -> login.loginPage("/login.jsf"))
                                        .authorizeRequests(config -> config
                                                        .authenticated("/v1/*")
                                                        .permit("/index.html")
                                                        .permit("/web/*"));

                        AuthorizationManager<HttpServletRequest> authorizationManager = new RequestMatcherAuthorizationManager(
                                        SecurityConfig.get().getRequestMatcherConfigurer());

                        Authentication authenticated = UserPasswordAutenticacion.authenticated("user", "password",
                                        GrantedPermission.empty);

                        Authentication notAuthenticated = UserPasswordAutenticacion.unauthenticated("user2",
                                        "password2");

                        HttpServletRequest request = new FakeHttpServerRequest("/paginanueva");
                        AuthorizationResult authorizationResult = authorizationManager.authorize(authenticated,
                                        request);
                        assertFalse(authorizationResult.isGranted());
                        authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                        assertFalse(authorizationResult.isGranted());

                        request = new FakeHttpServerRequest("/v1/getcontact/85");
                        authorizationResult = authorizationManager.authorize(authenticated, request);
                        assertTrue(authorizationResult.isGranted());
                        authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                        assertFalse(authorizationResult.isGranted());

                        request = new FakeHttpServerRequest("/web/nuvapagina");
                        authorizationResult = authorizationManager.authorize(authenticated, request);
                        assertTrue(authorizationResult.isGranted());
                        authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                        assertTrue(authorizationResult.isGranted());

                        request = FakeHttpServerRequest.builder().servletPath("/login.jsf").method("POST").build();

                        authorizationResult = authorizationManager.authorize(authenticated, request);
                        assertTrue(authorizationResult.isGranted());
                        authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                        assertTrue(authorizationResult.isGranted());

                        request = FakeHttpServerRequest.builder()
                                        .servletPath("/login.jsf")
                                        .queryString("error=AccessDenied")
                                        .method("POST").build();

                        authorizationResult = authorizationManager.authorize(authenticated, request);
                        assertTrue(authorizationResult.isGranted());
                        authorizationResult = authorizationManager.authorize(notAuthenticated, request);
                        assertTrue(authorizationResult.isGranted());

                } catch (Exception e) {
                        e.printStackTrace();
                        assertFalse(true);
                }
        }

}
