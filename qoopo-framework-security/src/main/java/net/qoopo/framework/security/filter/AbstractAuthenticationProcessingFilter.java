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
import net.qoopo.framework.security.SecurityConfig;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.failure.AuthenticationFailureStrategy;
import net.qoopo.framework.security.authentication.manager.AuthenticationManager;
import net.qoopo.framework.security.authentication.manager.ProviderManager;
import net.qoopo.framework.security.authentication.success.AuthenticationSuccessStrategy;
import net.qoopo.framework.security.context.SecurityContextHolder;
import net.qoopo.framework.security.matcher.RequestMatcher;
import net.qoopo.framework.security.matcher.RoutesDontRequiresAuthenticacionMatcher;
import net.qoopo.framework.security.matcher.RoutesRequiresAutenticationMatcher;

public abstract class AbstractAuthenticationProcessingFilter implements Filter {

    public static final Logger log = Logger.getLogger("Authentication filter");

    protected RequestMatcher requiresAuthenticationRequestMatcher = new RoutesRequiresAutenticationMatcher();

    protected RequestMatcher dontRequiresAuthenticationRequestMatcher = new RoutesDontRequiresAuthenticacionMatcher();

    protected AuthenticationFailureStrategy authenticationFailureStrategy;

    protected AuthenticationSuccessStrategy authenticationSuccessStrategy;

    protected AuthenticationManager authenticationManager = null;

    public abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response);

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
            log.warning("SecurityConfig is disabled");
            return;
        }

        loadConfig();
        try {
            // si llega aqui necesita realizar una autenticacion
            Authentication authentication = attemptAuthentication(request, response);

            if (!requiresAuthentication(request, response)) {
                log.warning("route dont require authentication [" + request.getServletPath() + "]");
                chain.doFilter(request, response);
                return;
            }

            // // si no se pudo autenticar
            // if (authentication == null || !authentication.isAuthenticated()) {
            // // delegamos la respuesta a una implementacion de no autorizado, esto puede
            // ser
            // // solicitar credenciales o mensaje de error
            // authenticationFailureStrategy.onFailureAuthentication(request, response);
            // return;
            // }
            successfulAuthentication(request, response, chain, authentication);
        } catch (AuthenticationException e) {
            unSuccessfulAuthentication(request, response, chain, e);
            // manejar error como Credenciales incorrectas
        }

        // Sección de autorización

    }

    /**
     * Comprueba si la solicitud requiere autenticacion
     * 
     * @param request
     * @param response
     * @return
     */
    private boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        boolean requires = !dontRequiresAuthenticationRequestMatcher.match(request);
        if (requires) {
            // valida si ya no está autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated())
                requires = false;
        }
        return requires;
    }

    /*
     * se encarga de cargar los recursos necesarios para la configuracion
     */
    private void loadConfig() {
        if (authenticationManager == null) {
            authenticationManager = new ProviderManager(SecurityConfig.get().getAuthenticationProviders());
        }
        if (authenticationFailureStrategy == null) {
            authenticationFailureStrategy = SecurityConfig.get().getFailureStrategy();
        }

        if (authenticationSuccessStrategy == null) {
            authenticationSuccessStrategy = SecurityConfig.get().getSuccesStrategy();
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
    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        authenticationSuccessStrategy.onSucessAuthentication(request, response, chain, authResult);
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
    private void unSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            AuthenticationException exception) throws IOException, ServletException {
        authenticationFailureStrategy.onFailureAuthentication(request, response, chain);
    }
}
