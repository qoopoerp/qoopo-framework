package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authorization.AuthorizationDeniedException;
import net.qoopo.framework.security.authorization.AuthorizationResult;
import net.qoopo.framework.security.authorization.manager.AuthorizationManager;
import net.qoopo.framework.security.authorization.manager.RequestMatcherAuthorizationManager;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.context.SecurityContextHolder;
import net.qoopo.framework.security.filter.strategy.failure.FailureStrategy;
import net.qoopo.framework.security.exception.SecurityException;

/**
 * 
 * filtro que se encarga de validar el accesso a una url
 * 
 * @author alberto
 */
@WebFilter(filterName = "authorizationFilter", urlPatterns = { "/*" })
public class AuthorizationFilter implements Filter {

    public static final Logger log = Logger.getLogger("AuthorizationFilter");

    private AuthorizationManager<HttpServletRequest> authorizationManager;

    protected FailureStrategy authenticationFailureStrategy;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!SecurityConfig.get().isEnabled()) {
            chain.doFilter(request, response);
            log.warning("SecurityConfig is disabled");
            return;
        }
        loadConfig();
        try {
            AuthorizationResult result = authorizationManager.authorize(this::getAuthentication, request);
            if (result != null && !result.isGranted()) {
                unSuccessfulAuthentication(request, response, chain, new AuthorizationDeniedException("AccessDenied"));
            } else
                chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            SecurityException exception) throws IOException, ServletException {
        log.warning("[+] authentication unsuccessful for " + request.getServletPath());
        if (authenticationFailureStrategy != null)
            authenticationFailureStrategy.onFailure(request, response, chain, exception);
        // throw new AuthorizationDeniedException("Access Denied");
    }

    /*
     * se encarga de cargar los recursos necesarios para la configuracion
     */
    private void loadConfig() {
        // si no está configurado un authentication manager creamos el predeterminado
        if (authorizationManager == null) {
            authorizationManager = new RequestMatcherAuthorizationManager(
                    SecurityConfig.get().getRequestMatcherConfigurer());
        }

        if (authenticationFailureStrategy == null) {
            authenticationFailureStrategy = SecurityConfig.get().getFailureAuthorizationStrategy();
        }
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // throw new AuthenticationNotFoundException(
            // "An Authentication object was not found in the SecurityContext");
        }
        return authentication;
    }

}
