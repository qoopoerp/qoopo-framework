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

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SecurityConfig {

    private static SecurityConfig INSTANCE = null;

    private boolean enabled = true;
    private AuthenticationManager authenticationManager = null;
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private RequestMatcherConfigurer requestMatcherConfigurer = new RequestMatcherConfigurer();
    private LoginConfigurer loginConfigurer = new LoginConfigurer();
    private AuthorizationConfigurer auhtorizationConfigurer = new AuthorizationConfigurer();

    public static final String version = "1.0.0-beta";

    private SecurityConfig() {

    }

    public static SecurityConfig get() {
        if (INSTANCE == null) {
            INSTANCE = new SecurityConfig();
            INSTANCE.login(login -> login.defaults());
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

    public SecurityConfig passwordEncoder(PasswordEncoder encoder) {
        if (encoder == null)
            throw new NullArgumentException();
        passwordEncoder = encoder;
        return this;
    }

    public SecurityConfig authorizeRequests(Supplier<RequestMatcherConfigurer> requestMatcher) {
        if (requestMatcher == null)
            throw new NullArgumentException();
        this.requestMatcherConfigurer = requestMatcher.get();
        return this;
    }

    public SecurityConfig authorizeRequests(
            Function<RequestMatcherConfigurer, RequestMatcherConfigurer> requestMatcher) {
        if (requestMatcher == null)
            throw new NullArgumentException();
        this.requestMatcherConfigurer = requestMatcher.apply(requestMatcherConfigurer);
        return this;
    }

    public SecurityConfig login(Supplier<LoginConfigurer> login) {
        if (login == null)
            throw new NullArgumentException();
        this.loginConfigurer = login.get();
        return this;
    }

    public SecurityConfig login(Function<LoginConfigurer, LoginConfigurer> login) {
        if (login == null)
            throw new NullArgumentException();
        this.loginConfigurer = login.apply(loginConfigurer);
        return this;
    }

    public SecurityConfig authorization(Supplier<AuthorizationConfigurer> login) {
        if (login == null)
            throw new NullArgumentException();
        this.auhtorizationConfigurer = login.get();
        return this;
    }

    public SecurityConfig authorization(Function<AuthorizationConfigurer, AuthorizationConfigurer> login) {
        if (login == null)
            throw new NullArgumentException();
        this.auhtorizationConfigurer = login.apply(auhtorizationConfigurer);
        return this;
    }

}
