package net.qoopo.framework.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.math3.exception.NullArgumentException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.manager.AuthenticationManager;
import net.qoopo.framework.security.authentication.password.encoder.BCryptPasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.PasswordEncoder;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.filter.strategy.failure.FailureStrategy;
import net.qoopo.framework.security.filter.strategy.failure.RedirectToLoginFailureStrategy;
import net.qoopo.framework.security.filter.strategy.failure.RedirectToPageFailureStrategy;
import net.qoopo.framework.security.filter.strategy.failure.SendErrorForbiddenStrategy;
import net.qoopo.framework.security.filter.strategy.success.InvalidateSessionStrategy;
import net.qoopo.framework.security.filter.strategy.success.MixSuccessStrategy;
import net.qoopo.framework.security.filter.strategy.success.RedirectToLoginSuccessStrategy;
import net.qoopo.framework.security.filter.strategy.success.RedirectToPageSuccessStrategy;
import net.qoopo.framework.security.filter.strategy.success.SuccessStrategy;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SecurityConfig {

    private static SecurityConfig INSTANCE = null;

    private boolean enabled = true;

    private String loginPage = "/login.html";
    private String logoutPage = "/logout";

    private SuccessStrategy succesAuthenticationStrategy = null;
    private FailureStrategy failureAuthenticationStrategy = new RedirectToLoginFailureStrategy();

    private SuccessStrategy succesLogoutStrategy = new MixSuccessStrategy(new RedirectToLoginSuccessStrategy(),
            new InvalidateSessionStrategy());
    private FailureStrategy failureLogoutStrategy = null;

    private FailureStrategy failureAuthorizationStrategy = new RedirectToLoginFailureStrategy();

    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
    private AuthenticationManager authenticationManager = null;
    private RequestMatcherConfigurer requestMatcherConfigurer = new RequestMatcherConfigurer();

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static final String version = "1.0.0-beta";

    private SecurityConfig() {
        loginPage(loginPage);
        logoutPage(logoutPage);
    }

    public static SecurityConfig get() {
        if (INSTANCE == null) {
            INSTANCE = new SecurityConfig();
        }
        return INSTANCE;
    }

    public SecurityConfig enable() {
        this.enabled = true;
        return this;
    }

    public SecurityConfig disable() {
        this.enabled = false;
        return this;
    }

    public SecurityConfig loginPage(String loginPage) {
        if (loginPage == null)
            throw new NullArgumentException();
        this.loginPage = loginPage;
        requestMatcher(config -> config.acceptRequest(loginPage));
        return this;
    }

    public SecurityConfig logoutPage(String logoutPage) {
        if (logoutPage == null)
            throw new NullArgumentException();
        this.logoutPage = logoutPage;
        requestMatcher(config -> config.acceptRequest(logoutPage));
        return this;
    }

    public SecurityConfig authenticationManager(AuthenticationManager manager) {
        if (manager == null)
            throw new NullArgumentException();
        authenticationManager = manager;
        return this;
    }

    public SecurityConfig registerAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        if (authenticationProvider == null)
            throw new NullArgumentException();
        authenticationProviders.add(authenticationProvider);
        return this;
    }

    public SecurityConfig successLoginUrl(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setSuccesAuthenticationStrategy(new RedirectToPageSuccessStrategy(url));
        return this;
    }

    public SecurityConfig failureLoginUrl(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(new RedirectToPageFailureStrategy(url));
        return this;
    }

    public SecurityConfig onSuccessAuthenticationStrategy(SuccessStrategy strategy) {
        if (strategy == null)
            throw new NullArgumentException();
        this.setSuccesAuthenticationStrategy(strategy);
        return this;
    }

    public SecurityConfig onFailureAuthenticationStrategy(FailureStrategy strategy) {
        if (strategy == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(strategy);
        return this;
    }

    public SecurityConfig onFailureAuthenticationRedirectToLogin() {
        this.setFailureAuthenticationStrategy(new RedirectToLoginFailureStrategy());
        return this;
    }

    public SecurityConfig onFailureAuthenticationRedirect(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(new RedirectToPageFailureStrategy(url));
        return this;
    }

    public SecurityConfig onFailureAuthenticationError() {
        this.setFailureAuthenticationStrategy(new SendErrorForbiddenStrategy());
        return this;
    }

    public SecurityConfig onFailureAuthenticationError(String message) {
        if (message == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(new SendErrorForbiddenStrategy(message));
        return this;
    }

    public SecurityConfig onFailureAuthorizationStrategy(FailureStrategy strategy) {
        if (strategy == null)
            throw new NullArgumentException();
        this.setFailureAuthorizationStrategy(strategy);
        return this;
    }

    public SecurityConfig onFailureAuthorizationRedirectToLogin() {
        this.setFailureAuthorizationStrategy(new RedirectToLoginFailureStrategy());
        return this;
    }

    public SecurityConfig onFailureAuthorizationRedirect(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setFailureAuthorizationStrategy(new RedirectToPageFailureStrategy(url));
        return this;
    }

    public SecurityConfig onFailureAuthorizationError() {
        this.setFailureAuthorizationStrategy(new SendErrorForbiddenStrategy());
        return this;
    }

    public SecurityConfig onFailureAuthorizationError(String message) {
        if (message == null)
            throw new NullArgumentException();
        this.setFailureAuthorizationStrategy(new SendErrorForbiddenStrategy(message));
        return this;
    }

    public SecurityConfig requestMatcher(Supplier<RequestMatcherConfigurer> requestMatcher) {
        if (requestMatcher == null)
            throw new NullArgumentException();
        this.requestMatcherConfigurer = requestMatcher.get();
        return this;
    }

    public SecurityConfig requestMatcher(Function<RequestMatcherConfigurer, RequestMatcherConfigurer> requestMatcher) {
        if (requestMatcher == null)
            throw new NullArgumentException();
        this.requestMatcherConfigurer = requestMatcher.apply(requestMatcherConfigurer);
        return this;
    }

    public SecurityConfig passwordEncoder(PasswordEncoder encoder) {
        if (encoder == null)
            throw new NullArgumentException();
        passwordEncoder = encoder;
        return this;
    }

}
