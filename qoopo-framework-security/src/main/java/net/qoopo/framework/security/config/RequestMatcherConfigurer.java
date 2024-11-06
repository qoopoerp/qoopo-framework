package net.qoopo.framework.security.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.qoopo.framework.security.authentication.matcher.AuthenticacionRequiredMatcher;
import net.qoopo.framework.security.authentication.matcher.PermitAllMatcher;
import net.qoopo.framework.security.matcher.RequestMatcher;
import net.qoopo.framework.security.matcher.UrlRequestMatcher;

/**
 * Realiza la configuración de los requestMatcher que serán aplicados
 */
public class RequestMatcherConfigurer {

    @Getter
    private List<RequestMatcher> requestMatchers = new ArrayList<>();

    public RequestMatcherConfigurer acceptRequestMatcher(RequestMatcher requestMatcher) {
        requestMatcher.setAuthenticationMatcher(new PermitAllMatcher());
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer denyRequestMatcher(RequestMatcher requestMatcher) {
        requestMatcher.setAuthenticationMatcher(null);
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer requireAuthenticatedRequestMatcher(RequestMatcher requestMatcher) {
        requestMatcher.setAuthenticationMatcher(new AuthenticacionRequiredMatcher());
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer acceptRequest(String... patterns) {
        for (String pattern : patterns) {
            acceptRequestMatcher(new UrlRequestMatcher(pattern, null));
        }
        return this;
    }

    public RequestMatcherConfigurer acceptRequestPostOnly(String... patterns) {
        for (String pattern : patterns) {
            acceptRequestMatcher(new UrlRequestMatcher(pattern, "POST"));
        }
        return this;
    }

    public RequestMatcherConfigurer acceptRequestGetOnly(String... patterns) {
        for (String pattern : patterns) {
            acceptRequestMatcher(new UrlRequestMatcher(pattern, "GET"));
        }
        return this;
    }

    public RequestMatcherConfigurer denyRequest(String... patterns) {
        for (String pattern : patterns) {
            denyRequestMatcher(new UrlRequestMatcher(pattern, null));
        }
        return this;
    }

    public RequestMatcherConfigurer denyRequestPostOnly(String... patterns) {
        for (String pattern : patterns) {
            denyRequestMatcher(new UrlRequestMatcher(pattern, "POST"));
        }
        return this;
    }

    public RequestMatcherConfigurer denyRequestGetOnly(String... patterns) {
        for (String pattern : patterns) {
            denyRequestMatcher(new UrlRequestMatcher(pattern, "GET"));
        }
        return this;
    }

    public RequestMatcherConfigurer requireAuthenticatedRequest(String... patterns) {
        for (String pattern : patterns) {
            requireAuthenticatedRequestMatcher(new UrlRequestMatcher(pattern, null));
        }
        return this;
    }

    public RequestMatcherConfigurer requireAuthenticatedRequestPostOnly(String... patterns) {
        for (String pattern : patterns) {
            requireAuthenticatedRequestMatcher(new UrlRequestMatcher(pattern, "POST"));
        }
        return this;
    }

    public RequestMatcherConfigurer requireAuthenticatedRequestGetOnly(String... patterns) {
        for (String pattern : patterns) {
            requireAuthenticatedRequestMatcher(new UrlRequestMatcher(pattern, "GET"));
        }
        return this;
    }

}
