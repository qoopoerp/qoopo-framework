package net.qoopo.framework.security.authentication.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Estrategia a implementar cuando hay un error de autenticacion
 */
public interface AuthenticationFailureStrategy {

    public void onFailureAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException;
}
