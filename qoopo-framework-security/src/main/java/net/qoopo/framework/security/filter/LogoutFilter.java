package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.context.SecurityContextHolder;
import net.qoopo.framework.security.exception.SecurityException;
import net.qoopo.framework.security.filter.strategy.failure.FailureStrategy;
import net.qoopo.framework.security.filter.strategy.success.SuccessStrategy;
import net.qoopo.framework.security.matcher.RequestMatcher;
import net.qoopo.framework.security.matcher.UrlRequestMatcher;

/**
 * Filtro que se encarga de realizar el proceso de logout
 */
public class LogoutFilter implements Filter {

    public static final Logger log = Logger.getLogger("Logout filter");

    protected RequestMatcher requiresLogoutRequestMatcher;

    protected FailureStrategy failureStrategy;

    protected SuccessStrategy successStrategy;

    // protected AuthenticationManager authenticationManager = null;

    /**
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     *
     * @exception IOException      if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (!SecurityConfig.get().isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        loadConfig();

        if (requiresLogout(request, response)) {
            try {
                log.info("[+] logout");
                successfulLogout(request, response, chain, SecurityContextHolder.getContext().getAuthentication());
            } catch (SecurityException e) {
                unSuccessfulLogout(request, response, chain, e);
            }
        }

        chain.doFilter(request, response);
        return;

    }

    /**
     * Indica si el filtro debe intentar realizar una autenticacion para la
     * solicitud.
     * 
     * Puede ser sobrecargado por el filtro en caso de requerir validaciones
     * adicionales
     * 
     * @param request
     * @param response
     * @return
     */
    private boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
        boolean requires = requiresLogoutRequestMatcher.matches(request);
        if (requires) {
            // valida que exita un authenticacion
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated())
                requires = false;
        }
        return requires;
    }

    /*
     * se encarga de cargar los recursos necesarios para la configuracion
     */
    protected void loadConfig() {
        if (failureStrategy == null) {
            failureStrategy = SecurityConfig.get().getFailureLogoutStrategy();
        }

        if (successStrategy == null) {
            successStrategy = SecurityConfig.get().getSuccesLogoutStrategy();
        }
        if (requiresLogoutRequestMatcher == null) {
            requiresLogoutRequestMatcher = new UrlRequestMatcher(SecurityConfig.get().getLogoutPage());
        }
    }

    /**
     * Realiza el proceso despues de conseguir una satisfacción satisfactoria
     * 
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    private void successfulLogout(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        log.info("[+] Logout successful");
        SecurityContextHolder.clear();
        if (successStrategy != null)
            successStrategy.onSucess(request, response, chain, authResult);
    }

    /**
     * Realiza el proceso despues de conseguir una autenticacion no satisfactoria
     * 
     * @param request
     * @param response
     * @param chain
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    private void unSuccessfulLogout(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            net.qoopo.framework.security.exception.SecurityException exception) throws IOException, ServletException {
        log.warning("[+] Logout unsuccessful " + exception.getLocalizedMessage());
        if (failureStrategy != null)
            failureStrategy.onFailure(request, response, chain, exception);
    }
}
