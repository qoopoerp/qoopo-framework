package net.qoopo.framework.security.config;

import org.apache.commons.math3.exception.NullArgumentException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.framework.security.filter.strategy.failure.FailureStrategy;
import net.qoopo.framework.security.filter.strategy.failure.RedirectToLoginFailureStrategy;
import net.qoopo.framework.security.filter.strategy.failure.RedirectToPageFailureStrategy;
import net.qoopo.framework.security.filter.strategy.failure.SendErrorForbiddenStrategy;

@Getter
@Setter
@NoArgsConstructor
public class AuthorizationConfigurer {

    private FailureStrategy failureAuthorizationStrategy = new RedirectToLoginFailureStrategy();

    public AuthorizationConfigurer onFailureAuthorizationStrategy(FailureStrategy strategy) {
        if (strategy == null)
            throw new NullArgumentException();
        this.setFailureAuthorizationStrategy(strategy);
        return this;
    }

    public AuthorizationConfigurer onFailureAuthorizationRedirectToLogin() {
        this.setFailureAuthorizationStrategy(new RedirectToLoginFailureStrategy());
        return this;
    }

    public AuthorizationConfigurer onFailureAuthorizationRedirect(String url) {
        if (url == null)
            throw new NullArgumentException();
        this.setFailureAuthorizationStrategy(new RedirectToPageFailureStrategy(url));
        return this;
    }

    public AuthorizationConfigurer onFailureAuthorizationError() {
        this.setFailureAuthorizationStrategy(new SendErrorForbiddenStrategy());
        return this;
    }

    public AuthorizationConfigurer onFailureAuthorizationError(String message) {
        if (message == null)
            throw new NullArgumentException();
        this.setFailureAuthorizationStrategy(new SendErrorForbiddenStrategy(message));
        return this;
    }

}
