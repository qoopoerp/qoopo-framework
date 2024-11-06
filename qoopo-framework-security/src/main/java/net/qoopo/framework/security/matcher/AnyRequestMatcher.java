package net.qoopo.framework.security.matcher;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Devuelve siempre verdadero
 */
public class AnyRequestMatcher extends AbstractRequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

}
