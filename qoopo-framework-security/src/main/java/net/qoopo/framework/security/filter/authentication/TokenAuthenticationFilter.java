package net.qoopo.framework.security.filter.authentication;

import java.util.List;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.password.BadCredentialsException;
import net.qoopo.framework.security.authentication.user.UserPasswordAutenticacion;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.core.token.Token;
import net.qoopo.framework.security.core.token.TokenProvider;
import net.qoopo.framework.security.filter.AbstractAuthenticationProcessingFilter;
import net.qoopo.framework.security.filter.strategy.failure.SendErrorUnathorizedStrategy;
import net.qoopo.framework.security.matcher.HttpHeadersMatcher;

/**
 * Filtro que se encarga de realizar la autenticación de los tokens
 *
 * @author alberto
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger log = Logger.getLogger("TokenAuthenticationFilter");

    private List<TokenProvider> tokenProviders;

    public TokenAuthenticationFilter() {
        super("TokenAuthenticationFilter");

    }

    /**
     * Sobrecarga la configuracion para setear el matcher del request
     */
    public void loadConfig() {
        if (tokenProviders == null)
            tokenProviders = SecurityConfig.get().getTokensProvidersConfigurer().getProviders();
        enabled = tokenProviders != null && !tokenProviders.isEmpty();
        // enabled = false;
        if (enabled)
            if (requiresAuthenticationRequestMatcher == null) {
                requiresAuthenticationRequestMatcher = new HttpHeadersMatcher(HttpHeaders.AUTHORIZATION, "Bearer ");
            }
        super.loadConfig();
        // cuanto hay una autenticacion exitosa en un token no debe realizar niguna
        // accion adicional como redirecciones (que si aplica a formularios login)
        super.authenticationSuccessStrategy = null;
        super.authenticationFailureStrategy = new SendErrorUnathorizedStrategy("NO AUTENTICADO");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // Recupera la cabecera HTTP Authorization de la peticion
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // // limpiamos cualquier contexto creado por alguna sesion
        // SecurityContextHolder.clear();

        boolean isValid = false;
        TokenProvider tokenProviderValid = null;
        for (TokenProvider tokenProvider : tokenProviders) {
            if (tokenProvider.validate(authorizationHeader)) {
                isValid = true;
                tokenProviderValid = tokenProvider;
                break;
            }
        }
        if (!isValid || tokenProviderValid == null) {
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
            throw new BadCredentialsException("UNAUTHORIZED");
        }
        Token token = tokenProviderValid.getToken(authorizationHeader);
        return UserPasswordAutenticacion.authenticated(token.getPrincipal(), null, token.getPermissions());
    }

}
