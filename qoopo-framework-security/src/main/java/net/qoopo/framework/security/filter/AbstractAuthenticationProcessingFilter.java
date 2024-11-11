package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.manager.AuthenticationManager;
import net.qoopo.framework.security.authentication.manager.ProviderManager;
import net.qoopo.framework.security.authentication.session.SessionAuthenticationStrategy;
import net.qoopo.framework.security.authentication.session.SimpleAuthenticationSessionStrategy;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.context.SecurityContext;
import net.qoopo.framework.security.context.SecurityContextHolder;
import net.qoopo.framework.security.filter.strategy.failure.FailureStrategy;
import net.qoopo.framework.security.filter.strategy.success.SuccessStrategy;
import net.qoopo.framework.security.matcher.RequestMatcher;

/**
 * Filtro abstracto que debe ser implementado (heredado) en un filtros
 * específicos para realizar intentos de Autenticacion
 */
public abstract class AbstractAuthenticationProcessingFilter extends OncePerRequestFilter {

    public static final Logger log = Logger.getLogger("Authentication filter");

    protected RequestMatcher requiresAuthenticationRequestMatcher;

    protected FailureStrategy authenticationFailureStrategy;

    protected SuccessStrategy authenticationSuccessStrategy;

    protected SessionAuthenticationStrategy sessionStrategy = new SimpleAuthenticationSessionStrategy();

    protected AuthenticationManager authenticationManager = null;

    public abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response);

    public AbstractAuthenticationProcessingFilter(String name) {
        super(name);
    }

    protected void doInternalFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        loadConfig();

        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (SecurityConfig.get().isDebug())
                log.info("[*] Authentication is required [" + name + "]");

            // si llega aqui necesita realizar una autenticacion
            Authentication authentication = attemptAuthentication(request, response);
            if (authentication != null && authentication.isAuthenticated()) {
                sessionStrategy.onAuthentication(authentication, request, response);
                successfulAuthentication(request, response, chain, authentication);
            } else {
                unSuccessfulAuthentication(request, response, chain, null);
            }
        } catch (AuthenticationException e) {
            unSuccessfulAuthentication(request, response, chain, e);
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
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        boolean requires = requiresAuthenticationRequestMatcher != null
                && requiresAuthenticationRequestMatcher.matches(request);
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
    protected void loadConfig() {
        if (enabled) {
            // tomamos el authentication manager configurado
            if (authenticationManager == null) {
                authenticationManager = SecurityConfig.get().getAuthenticationManager();
            }
            // si no está configurado un authentication manager creamos el predeterminado
            if (authenticationManager == null) {
                if (SecurityConfig.get().getAuthenticationProviders() != null
                        && !SecurityConfig.get().getAuthenticationProviders().isEmpty()) {
                    authenticationManager = new ProviderManager(SecurityConfig.get().getAuthenticationProviders());
                } else {
                    // inicia un manager in providers registrados, cada filtro deberá agregar un
                    // provider que encuentre
                    authenticationManager = new ProviderManager(new ArrayList<>());
                }
            }

            if (authenticationFailureStrategy == null) {
                authenticationFailureStrategy = SecurityConfig.get().getLoginConfigurer()
                        .getFailureAuthenticationStrategy();
            }

            if (authenticationSuccessStrategy == null) {
                authenticationSuccessStrategy = SecurityConfig.get().getLoginConfigurer()
                        .getSuccesAuthenticationStrategy();
            }
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
        if (SecurityConfig.get().isDebug())
            log.info("[+] Authentication successful");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        if (authenticationSuccessStrategy != null)
            authenticationSuccessStrategy.onSucess(request, response, chain, authResult);
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
        if (SecurityConfig.get().isDebug())
            log.warning("[!] Authentication unsuccessful -> name:" + name + " -> Exception: " + exception);
        SecurityContextHolder.clear();
        if (authenticationFailureStrategy != null)
            authenticationFailureStrategy.onFailure(request, response, chain, exception);
    }
}
