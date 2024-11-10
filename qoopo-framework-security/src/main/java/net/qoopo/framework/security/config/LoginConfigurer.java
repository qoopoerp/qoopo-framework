package net.qoopo.framework.security.config;

import org.apache.commons.math3.exception.NullArgumentException;

import lombok.Getter;
import lombok.Setter;
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
public class LoginConfigurer {
    private String loginPage;
    private String logoutPage;

    private SuccessStrategy succesAuthenticationStrategy = null;
    private FailureStrategy failureAuthenticationStrategy = new RedirectToLoginFailureStrategy();

    private SuccessStrategy succesLogoutStrategy = new MixSuccessStrategy(new RedirectToLoginSuccessStrategy(),
            new InvalidateSessionStrategy());
    private FailureStrategy failureLogoutStrategy = null;

    public LoginConfigurer() {

    }

    public LoginConfigurer defaults() {
        loginPage("/login.html");
        logoutPage("/logout");
        succesAuthenticationStrategy = null;
        failureAuthenticationStrategy = new RedirectToLoginFailureStrategy();
        succesLogoutStrategy = new MixSuccessStrategy(new RedirectToLoginSuccessStrategy(),
                new InvalidateSessionStrategy());
        failureLogoutStrategy = null;
        return this;
    }

    public LoginConfigurer loginPage(String loginPage) {
        if (loginPage == null)
            throw new NullArgumentException();
        this.loginPage = loginPage;
        SecurityConfig.get().authorizeRequests(config -> config.permit(loginPage)
                .permit("/css/*", "/js/*", "*.js", "*.css"));
        return this;
    }

    public LoginConfigurer logoutPage(String logoutPage) {
        if (logoutPage == null)
            throw new NullArgumentException();
        this.logoutPage = logoutPage;
        SecurityConfig.get().authorizeRequests(config -> config.permit(logoutPage));
        return this;
    }

    public LoginConfigurer successLoginUrl(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setSuccesAuthenticationStrategy(new RedirectToPageSuccessStrategy(url));
        return this;
    }

    public LoginConfigurer failureLoginUrl(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(new RedirectToPageFailureStrategy(url));
        return this;
    }

    public LoginConfigurer onSuccessAuthenticationStrategy(SuccessStrategy strategy) {
        if (strategy == null)
            throw new NullArgumentException();
        this.setSuccesAuthenticationStrategy(strategy);
        return this;
    }

    public LoginConfigurer onFailureAuthenticationStrategy(FailureStrategy strategy) {
        if (strategy == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(strategy);
        return this;
    }

    public LoginConfigurer onFailureAuthenticationRedirectToLogin() {
        this.setFailureAuthenticationStrategy(new RedirectToLoginFailureStrategy());
        return this;
    }

    public LoginConfigurer onFailureAuthenticationRedirect(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(new RedirectToPageFailureStrategy(url));
        return this;
    }

    public LoginConfigurer onFailureAuthenticationError() {
        this.setFailureAuthenticationStrategy(new SendErrorForbiddenStrategy());
        return this;
    }

    public LoginConfigurer onFailureAuthenticationError(String message) {
        if (message == null)
            throw new NullArgumentException();
        this.setFailureAuthenticationStrategy(new SendErrorForbiddenStrategy(message));
        return this;
    }
}
