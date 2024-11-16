package net.qoopo.framework.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.exception.NullArgumentException;
import net.qoopo.framework.security.authentication.manager.AuthenticationManager;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.session.NullAuthenticationSessionStrategy;
import net.qoopo.framework.security.authentication.session.SessionAuthenticationStrategy;
import net.qoopo.framework.security.authentication.session.SimpleAuthenticationSessionStrategy;
import net.qoopo.framework.security.core.encoder.BCryptPasswordEncoder;
import net.qoopo.framework.security.core.encoder.PasswordEncoder;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SecurityConfig {

    private static SecurityConfig INSTANCE = null;

    private boolean debug = false;
    private boolean enabled = true;
    private AuthenticationManager authenticationManager = null;
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private RequestMatcherConfigurer requestMatcherConfigurer = new RequestMatcherConfigurer();
    private LoginConfigurer loginConfigurer = new LoginConfigurer();
    private AuthorizationConfigurer auhtorizationConfigurer = new AuthorizationConfigurer();
    private TokensProvidersConfigurer tokensProvidersConfigurer = new TokensProvidersConfigurer();
    protected SessionAuthenticationStrategy sessionStrategy = new SimpleAuthenticationSessionStrategy();

    public static final String version = "1.0.0-beta";

    private SecurityConfig() {

    }

    public static SecurityConfig get() {
        if (INSTANCE == null) {
            INSTANCE = new SecurityConfig();
        }
        return INSTANCE;
    }

    public SecurityConfig debug() {
        this.debug = true;
        return this;
    }

    public SecurityConfig noDebug() {
        this.debug = false;
        return this;
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

    public SecurityConfig tokens(Supplier<TokensProvidersConfigurer> tokens) {
        if (tokens == null)
            throw new NullArgumentException();
        this.tokensProvidersConfigurer = tokens.get();
        return this;
    }

    public SecurityConfig tokens(Function<TokensProvidersConfigurer, TokensProvidersConfigurer> tokens) {
        if (tokens == null)
            throw new NullArgumentException();
        this.tokensProvidersConfigurer = tokens.apply(tokensProvidersConfigurer);
        return this;
    }

    public SecurityConfig sessionStrategy(SessionAuthenticationStrategy strategy) {
        if (strategy == null)
            throw new NullArgumentException();
        sessionStrategy = strategy;
        return this;
    }

    public SecurityConfig sessionOnAuthentication() {
        sessionStrategy = new SimpleAuthenticationSessionStrategy();
        return this;
    }

    public SecurityConfig sessionNull() {
        sessionStrategy = new NullAuthenticationSessionStrategy();
        return this;
    }

}
