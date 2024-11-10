package net.qoopo.framework.security.authentication.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;

/**
 * Define la estregia a implementar cuando una autenticación ocurre
 */
public interface SessionAuthenticationStrategy {
    
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws SessionAuthenticationException;
}
