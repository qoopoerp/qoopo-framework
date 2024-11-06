package net.qoopo.framework.security.matcher;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Devuelve verdadero si todos los RequestMatcher que contiene da verdadero
 */
public class AndRequestMatcher extends AbstractRequestMatcher {

    private List<RequestMatcher> requestMatcherList;

    public AndRequestMatcher(RequestMatcher... requestMatchers) {
        this(Arrays.asList(requestMatchers));
    }

    public AndRequestMatcher(List<RequestMatcher> requestMatchers) {
        this.requestMatcherList = requestMatchers;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        for (RequestMatcher requestMatcher : requestMatcherList) {
            if (!requestMatcher.matches(request))
                return false;
        }
        return true;
    }

}
