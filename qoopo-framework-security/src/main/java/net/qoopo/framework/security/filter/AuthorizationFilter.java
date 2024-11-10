package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authorization.AuthorizationDeniedException;
import net.qoopo.framework.security.authorization.AuthorizationResult;
import net.qoopo.framework.security.authorization.manager.AuthorizationManager;
import net.qoopo.framework.security.authorization.manager.RequestMatcherAuthorizationManager;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.context.SecurityContextHolder;
import net.qoopo.framework.security.exception.SecurityException;
import net.qoopo.framework.security.filter.strategy.failure.FailureStrategy;

/**
 * 
 * filtro que se encarga de validar el accesso a una url
 * 
 * @author alberto
 */
// @WebFilter(filterName = "filter_2_authorizationFilter", urlPatterns = { "/*" })
public class AuthorizationFilter extends AbstractSecurityFilter {

    public static final Logger log = Logger.getLogger("AuthorizationFilter");

    private AuthorizationManager<HttpServletRequest> authorizationManager;

    protected FailureStrategy authenticationFailureStrategy;

    public AuthorizationFilter() {
        super("authorizationFilter");
    }

    protected void doInternalFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        loadConfig();
        try {
            AuthorizationResult result = authorizationManager.authorize(getAuthentication(), request);
            if (result != null && !result.isGranted()) {
                unSuccessfulAuthorization(request, response, chain, new AuthorizationDeniedException("AccessDenied"));
            } else
                chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unSuccessfulAuthorization(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            SecurityException exception) throws IOException, ServletException {
        log.warning("[+] Authorization unsuccessful for " + request.getServletPath());
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
            authenticationFailureStrategy = SecurityConfig.get().getAuhtorizationConfigurer()
                    .getFailureAuthorizationStrategy();
        }
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.warning("[!] No existe una autenticación");
        }
        return authentication;
    }

}
