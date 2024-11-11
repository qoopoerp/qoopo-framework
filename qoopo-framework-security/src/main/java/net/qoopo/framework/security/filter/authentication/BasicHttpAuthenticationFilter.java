package net.qoopo.framework.security.filter.authentication;

import java.util.Base64;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.matcher.BasicHttpAuthenticationMatcher;

/**
 * Filtro que intentar autenticar cuando hay una solicitud de autneticación
 * basica http
 *
 * @author alberto
 */
public class BasicHttpAuthenticationFilter extends AbstractUserPasswordAuthenticationFilter {

    private static Logger log = Logger.getLogger("BasicHttpAuthenticationFilter");

    public BasicHttpAuthenticationFilter() {
        super("BasicHttpAuthenticationFilter");
        onlyPost = false;
    }

    public BasicHttpAuthenticationFilter(boolean onlyPost) {
        super("BasicHttpAuthenticationFilter", onlyPost);
        onlyPost = false;
    }

    protected String obtainUsername(HttpServletRequest request) {
        String userName = decodeBasicAuthToken(request.getHeader("Authorization"))[0];
        return userName;
    }

    protected String obtainPassword(HttpServletRequest request) {
        String password = decodeBasicAuthToken(request.getHeader("Authorization"))[1];
        return password;
    }

    private String[] decodeBasicAuthToken(String authHeader) {
        // Remover "Basic " del encabezado
        log.info("Authentication header: " + authHeader);
        String base64Credentials = authHeader.substring("Basic ".length());
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedCredentials = new String(decodedBytes);
        // Dividir las credenciales en [usuario, contraseña]
        return decodedCredentials.split(":", 2);
    }

    /**
     * Sobrecarga la configuracion para setear el matcher del request
     */
    public void loadConfig() {
        enabled = SecurityConfig.get().getLoginConfigurer().isBasicHttp();
        log.info(" enabled? " + enabled);
        if (enabled)
            if (requiresAuthenticationRequestMatcher == null) {
                requiresAuthenticationRequestMatcher = new BasicHttpAuthenticationMatcher();
            }
        super.loadConfig();
    }

}
