package net.qoopo.framework.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.authentication.matcher.AuthenticationMatcher;

public abstract class AbstractRequestMatcher implements RequestMatcher {

    private AuthenticationMatcher authenticationMatcher;

    @Override
    public abstract boolean matches(HttpServletRequest request);

    @Override
    public AuthenticationMatcher getAuthenticationMatcher() {
        return authenticationMatcher;
    }

    @Override
    public void setAuthenticationMatcher(AuthenticationMatcher authenticationMatcher) {
        this.authenticationMatcher = authenticationMatcher;
    }

}
