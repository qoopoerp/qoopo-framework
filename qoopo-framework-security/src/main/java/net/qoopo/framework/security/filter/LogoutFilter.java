package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

// @WebFilter(filterName = "filter_3_logoutFilter", urlPatterns = { "/*" })
public class LogoutFilter extends OncePerRequestFilter {

    public static final Logger log = Logger.getLogger("Logout filter");

    protected RequestMatcher requiresLogoutRequestMatcher;

    protected FailureStrategy failureStrategy;

    protected SuccessStrategy successStrategy;

    public LogoutFilter() {
        super("logoutFilter");
    }

    protected void doInternalFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
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
        } else {
            chain.doFilter(request, response);
        }

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
            log.info("[+] se requiere un logout");
            // valida que exista un authenticacion
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
            failureStrategy = SecurityConfig.get().getLoginConfigurer().getFailureLogoutStrategy();
        }
        if (successStrategy == null) {
            successStrategy = SecurityConfig.get().getLoginConfigurer().getSuccesLogoutStrategy();
        }
        if (requiresLogoutRequestMatcher == null) {

            // si no encuentra una LoginPage usa la default del framework
            if (SecurityConfig.get().getLoginConfigurer().getLogoutPage() == null) {
                SecurityConfig.get().login(login -> login.logoutPage("/logout"));
            }

            requiresLogoutRequestMatcher = new UrlRequestMatcher(
                    SecurityConfig.get().getLoginConfigurer().getLogoutPage());
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
