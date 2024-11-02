package net.qoopo.framework.security.authentication.success;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;

/**
 * Estrategia a implementar cuadno hay una authenticacion satisfactoria
 */
public interface AuthenticationSuccessStrategy {

    /**
     * 
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    public void onSucessAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException;

}
