package net.qoopo.framework.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.authentication.matcher.AuthenticationMatcher;

public interface RequestMatcher {
    public boolean matches(HttpServletRequest request);

    public AuthenticationMatcher getAuthenticationMatcher();

    public void setAuthenticationMatcher(AuthenticationMatcher authenticationMatcher);
}
