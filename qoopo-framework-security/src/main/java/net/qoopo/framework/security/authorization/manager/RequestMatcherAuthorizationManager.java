package net.qoopo.framework.security.authorization.manager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

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

    private List<RequestMatcher> requestMatchers;

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
    public AuthorizationResult authorize(Supplier<Authentication> authentication, HttpServletRequest request) {
        boolean granted = isGranted(authentication.get(), request);
        return new AuthorizationResponse(granted);
    }

    private boolean isGranted(Authentication authentication, HttpServletRequest request) {
        for (RequestMatcher matcher : requestMatchers) {
            if (matcher.matches(request) && matcher.getAuthenticationMatcher().matches(authentication)) {
                return true;
            }
        }
        return false;
    }

}
