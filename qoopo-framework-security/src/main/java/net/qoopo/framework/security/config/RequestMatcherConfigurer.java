package net.qoopo.framework.security.config;

import java.beans.DefaultPersistenceDelegate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.qoopo.framework.security.authentication.matcher.AuthenticacionRequiredMatcher;
import net.qoopo.framework.security.authentication.matcher.AuthenticationMatcher;
import net.qoopo.framework.security.authentication.matcher.DenyAllMatcher;
import net.qoopo.framework.security.authentication.matcher.HasAnyRoleMatcher;
import net.qoopo.framework.security.authentication.matcher.HasRoleMatcher;
import net.qoopo.framework.security.authentication.matcher.PermitAllMatcher;
import net.qoopo.framework.security.core.permission.DefaultGrantedPermission;
import net.qoopo.framework.security.matcher.IpAddressMatcher;
import net.qoopo.framework.security.matcher.RequestMatcher;
import net.qoopo.framework.security.matcher.UrlRequestMatcher;

/**
 * Realiza la configuración de los requestMatcher que serán aplicados
 */
public class RequestMatcherConfigurer {

    private static AuthenticationMatcher permitAllMatcher = new PermitAllMatcher();
    private static AuthenticationMatcher denyAllMatcher = new DenyAllMatcher();
    private static AuthenticationMatcher requireAuthenticationMatcher = new AuthenticacionRequiredMatcher();

    @Getter
    private List<RequestMatcher> requestMatchers = new ArrayList<>();

    public RequestMatcherConfigurer hasRole(RequestMatcher requestMatcher, String... roles) {
        requestMatcher.setAuthenticationMatcher(new HasRoleMatcher(roles));
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer hasAnyRole(RequestMatcher requestMatcher, String... roles) {
        requestMatcher.setAuthenticationMatcher(new HasAnyRoleMatcher(roles));
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer hasAnyRole(RequestMatcher requestMatcher, List<String> roles) {
        requestMatcher.setAuthenticationMatcher(new HasAnyRoleMatcher(DefaultGrantedPermission.of(roles)));
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer permitMatcher(RequestMatcher requestMatcher) {
        requestMatcher.setAuthenticationMatcher(permitAllMatcher);
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer denyMatcher(RequestMatcher requestMatcher) {
        requestMatcher.setAuthenticationMatcher(denyAllMatcher);
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer authenticatedMatcher(RequestMatcher requestMatcher) {
        requestMatcher.setAuthenticationMatcher(requireAuthenticationMatcher);
        requestMatchers.add(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer removeMatcher(RequestMatcher requestMatcher) {
        requestMatchers.remove(requestMatcher);
        return this;
    }

    public RequestMatcherConfigurer remove(String... patterns) {
        for (String pattern : patterns) {
            removeMatcher(new UrlRequestMatcher(pattern));
        }
        return this;
    }

    public RequestMatcherConfigurer permit(String... patterns) {
        for (String pattern : patterns) {
            permitMatcher(new UrlRequestMatcher(pattern));
        }
        return this;
    }

    public RequestMatcherConfigurer permitPostOnly(String... patterns) {
        for (String pattern : patterns) {
            permitMatcher(new UrlRequestMatcher(pattern, "POST"));
        }
        return this;
    }

    public RequestMatcherConfigurer permitGetOnly(String... patterns) {
        for (String pattern : patterns) {
            permitMatcher(new UrlRequestMatcher(pattern, "GET"));
        }
        return this;
    }

    public RequestMatcherConfigurer denyRequest(String... patterns) {
        for (String pattern : patterns) {
            denyMatcher(new UrlRequestMatcher(pattern));
        }
        return this;
    }

    public RequestMatcherConfigurer denyPostOnly(String... patterns) {
        for (String pattern : patterns) {
            denyMatcher(new UrlRequestMatcher(pattern, "POST"));
        }
        return this;
    }

    public RequestMatcherConfigurer denyGetOnly(String... patterns) {
        for (String pattern : patterns) {
            denyMatcher(new UrlRequestMatcher(pattern, "GET"));
        }
        return this;
    }

    public RequestMatcherConfigurer authenticated(String... patterns) {
        for (String pattern : patterns) {
            authenticatedMatcher(new UrlRequestMatcher(pattern));
        }
        return this;
    }

    public RequestMatcherConfigurer authenticatedPostOnly(String... patterns) {
        for (String pattern : patterns) {
            authenticatedMatcher(new UrlRequestMatcher(pattern, "POST"));
        }
        return this;
    }

    public RequestMatcherConfigurer authenticatedGetOnly(String... patterns) {
        for (String pattern : patterns) {
            authenticatedMatcher(new UrlRequestMatcher(pattern, "GET"));
        }
        return this;
    }

    // configuraciones por ip

    public RequestMatcherConfigurer permitIp(String... patterns) {
        for (String pattern : patterns) {
            permitMatcher(new IpAddressMatcher(pattern));
        }
        return this;
    }

    public RequestMatcherConfigurer denyIp(String... patterns) {
        for (String pattern : patterns) {
            denyMatcher(new IpAddressMatcher(pattern));
        }
        return this;
    }

    public RequestMatcherConfigurer authenticatedIp(String... patterns) {
        for (String pattern : patterns) {
            authenticatedMatcher(new IpAddressMatcher(pattern));
        }
        return this;
    }

    // roles
    public RequestMatcherConfigurer hasRole(String pattern, String... roles) {
        hasRole(new UrlRequestMatcher(pattern), roles);
        return this;
    }

    public RequestMatcherConfigurer hasRole(List<String> patterns, String... roles) {
        for (String pattern : patterns) {
            hasRole(new UrlRequestMatcher(pattern), roles);
        }
        return this;
    }

    public RequestMatcherConfigurer hasRolePostOnly(List<String> patterns, String... roles) {
        for (String pattern : patterns) {
            hasRole(new UrlRequestMatcher(pattern, "POST"), roles);
        }
        return this;
    }

    public RequestMatcherConfigurer hasRoleGetOnly(List<String> patterns, String... roles) {
        for (String pattern : patterns) {
            hasRole(new UrlRequestMatcher(pattern, "GET"), roles);
        }
        return this;
    }

    // --- HasAnyRol

    public RequestMatcherConfigurer hasAnyRole(List<String> patterns, String... roles) {
        for (String pattern : patterns) {
            hasAnyRole(new UrlRequestMatcher(pattern), roles);
        }
        return this;
    }

    public RequestMatcherConfigurer hasAnyRole(List<String> patterns, List<String> roles) {
        for (String pattern : patterns) {
            hasAnyRole(new UrlRequestMatcher(pattern), roles);
        }
        return this;
    }

    public RequestMatcherConfigurer hasAnyRolePostOnly(List<String> patterns, String... roles) {
        for (String pattern : patterns) {
            hasAnyRole(new UrlRequestMatcher(pattern, "POST"), roles);
        }
        return this;
    }

    public RequestMatcherConfigurer hasAnyRoleGetOnly(List<String> patterns, String... roles) {
        for (String pattern : patterns) {
            hasAnyRole(new UrlRequestMatcher(pattern, "GET"), roles);
        }
        return this;
    }

}
