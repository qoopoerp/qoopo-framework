package net.qoopo.framework.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NullArgumentException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.failure.AuthenticationFailureStrategy;
import net.qoopo.framework.security.authentication.failure.RedirectToLoginFailureStrategy;
import net.qoopo.framework.security.authentication.failure.RedirectToPageFailureStrategy;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.success.AuthenticationSuccessStrategy;
import net.qoopo.framework.security.authentication.success.RedirectToPageSuccessStrategy;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SecurityConfig {

    private static SecurityConfig INSTANCE = null;

    @Builder.Default
    private boolean enabled = false;
    @Builder.Default
    private String loginPage = "/login.jsf";
    @Builder.Default
    private String publicPage = "/home.jsf";
    @Builder.Default
    private String invalidPage = "/login.jsf";

    private AuthenticationSuccessStrategy succesStrategy = null;
    private AuthenticationFailureStrategy failureStrategy = new RedirectToLoginFailureStrategy();

    // private AuthenticationManager authenticationManager;
    @Builder.Default
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();

    // @Builder.Default
    // private SecurityConfigRouter routes;

    public static final String version = "1.0.0-beta";

    private SecurityConfig() {
        //
    }

    public static void config(SecurityConfig qf) {
        INSTANCE = qf;
    }

    public static SecurityConfig get() {
        if (INSTANCE == null) {
            INSTANCE = new SecurityConfig();
        }
        return INSTANCE;
    }

    public SecurityConfig registerAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        if (authenticationProvider == null)
            throw new NullArgumentException();
        authenticationProviders.add(authenticationProvider);
        return this;
    }

    public SecurityConfig successUrl(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setSuccesStrategy(new RedirectToPageSuccessStrategy(url));
        return this;
    }

    public SecurityConfig failureUrl(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setFailureStrategy(new RedirectToPageFailureStrategy(url));
        return this;
    }

}
