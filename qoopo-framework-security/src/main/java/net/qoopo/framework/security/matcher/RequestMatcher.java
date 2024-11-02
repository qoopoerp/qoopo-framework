package net.qoopo.framework.security.matcher;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {
    public boolean match(HttpServletRequest request);
}
