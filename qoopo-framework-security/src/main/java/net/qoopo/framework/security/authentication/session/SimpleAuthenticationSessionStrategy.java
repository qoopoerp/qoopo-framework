package net.qoopo.framework.security.authentication.session;

import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.qoopo.framework.security.authentication.Authentication;

/**
 * No realiza ninguna accion con la sesion
 */
public class SimpleAuthenticationSessionStrategy implements SessionAuthenticationStrategy {

    private static Logger log = Logger.getLogger("SimpleAuthenticationSessionStrategy");

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
            HttpServletResponse response) throws SessionAuthenticationException {
        log.info("[+] Creando una session nueva");
        // crea una sesion nueva
        HttpSession session = request.getSession(true);
        log.info("Sesión creada: " + session.getId());
    }

}
