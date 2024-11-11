package net.qoopo.framework.security.filter.strategy.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Realiza una solicitud de autenticación basica http
 */
public class RequestBasicAuthenticationFailureStrategy implements FailureStrategy {

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            net.qoopo.framework.security.exception.SecurityException exception)
            throws ServletException, IOException {

        // Solicitar credenciales con un 401 Unauthorized y el encabezado
        // WWW-Authenticate
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"Protected\"");
        response.getWriter().write("Se requieren credenciales de autenticación básica");

    }

}
