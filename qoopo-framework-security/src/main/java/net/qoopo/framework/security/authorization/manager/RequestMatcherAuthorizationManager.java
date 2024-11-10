package net.qoopo.framework.security.authorization.manager;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authorization.AuthorizationResponse;
import net.qoopo.framework.security.authorization.AuthorizationResult;
import net.qoopo.framework.security.config.RequestMatcherConfigurer;
import net.qoopo.framework.security.matcher.RequestMatcher;

/**
 * Realiza la validación de la autorizacion en funcion de las rutas configuradas
 */
public class RequestMatcherAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private static final int AND = 1;
    private static final int OR = 0;

    private List<RequestMatcher> requestMatchers;
    private int type = OR;

    public RequestMatcherAuthorizationManager(RequestMatcherConfigurer configurer) {
        this.requestMatchers = configurer.getRequestMatchers();
    }

    public RequestMatcherAuthorizationManager(RequestMatcher... requestMatchers) {
        this(Arrays.asList(requestMatchers));
    }

    public RequestMatcherAuthorizationManager(List<RequestMatcher> requestMatchers) {
        this.requestMatchers = requestMatchers;
    }

    @Override
    public AuthorizationResult authorize(Authentication authentication, HttpServletRequest request) {
        boolean granted = isGranted(authentication, request);
        return new AuthorizationResponse(granted);
    }

    private boolean isGranted(Authentication authentication, HttpServletRequest request) {
        switch (type) {
            case AND:
                return validateAnd(authentication, request);
            default:
                return validateOr(authentication, request);
        }
    }

    private boolean validateAnd(Authentication authentication, HttpServletRequest request) {
        for (RequestMatcher matcher : requestMatchers) {
            boolean urlMatch = matcher.matches(request);
            boolean authenticationMatch = matcher.getAuthenticationMatcher().matches(authentication);
            if (!urlMatch && !authenticationMatch)
                return false;
        }
        return true;
    }

    private boolean validateOr(Authentication authentication, HttpServletRequest request) {
        for (RequestMatcher matcher : requestMatchers) {
            boolean urlMatch = matcher.matches(request);
            boolean authenticationMatch = matcher.getAuthenticationMatcher().matches(authentication);
            if (urlMatch && authenticationMatch)
                return true;
        }
        return false;
    }

}
