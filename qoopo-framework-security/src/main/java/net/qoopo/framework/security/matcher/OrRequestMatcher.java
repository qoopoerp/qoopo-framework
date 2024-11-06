package net.qoopo.framework.security.matcher;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Devuelve verdadero si uno de los RequestMatcher que contiene da verdadero
 */
public class OrRequestMatcher extends AbstractRequestMatcher {

    private List<RequestMatcher> requestMatcherList;

    public OrRequestMatcher(RequestMatcher... requestMatchers) {
        this(Arrays.asList(requestMatchers));
    }

    public OrRequestMatcher(List<RequestMatcher> requestMatchers) {
        this.requestMatcherList = requestMatchers;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        for (RequestMatcher requestMatcher : requestMatcherList) {
            if (requestMatcher.matches(request))
                return true;
        }
        return false;
    }

}
