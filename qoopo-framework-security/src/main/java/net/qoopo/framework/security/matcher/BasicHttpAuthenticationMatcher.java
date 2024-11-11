package net.qoopo.framework.security.matcher;

import java.util.Base64;
import java.util.logging.Logger;

import java.util.Arrays;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Valida la solicitud en función de un patrón
 * 
 * Si el patron es "/**" se valida como verdadero a todas las rutas
 */

public class BasicHttpAuthenticationMatcher extends AbstractRequestMatcher {

    private static Logger log = Logger.getLogger("BasicHttpAuthenticationMatcher");

    public BasicHttpAuthenticationMatcher() {

    }

    @Override
    public boolean matches(HttpServletRequest request) {

        log.info("- consultando si reuiere autenticacion ");

        String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                String[] args = decodeBasicAuthToken(authHeader);
                log.info("-> " + (args != null && args.length == 2 && args[0] != null && !args[0].isEmpty()) + " -"
                        + Arrays.toString(args));
                return args != null && args.length == 2 && args[0] != null && !args[0].isEmpty();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        log.info("->false");
        return false;
        // return authHeader != null && authHeader.startsWith("Basic ");
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
}
