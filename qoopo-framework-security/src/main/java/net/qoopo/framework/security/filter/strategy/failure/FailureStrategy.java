package net.qoopo.framework.security.filter.strategy.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Estrategia a implementar cuando hay un error de autenticacion
 */
public interface FailureStrategy {

    public void onFailure(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            net.qoopo.framework.security.exception.SecurityException exception)
            throws ServletException, IOException;
}
