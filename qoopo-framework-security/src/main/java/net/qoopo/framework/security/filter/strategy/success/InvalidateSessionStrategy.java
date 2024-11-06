package net.qoopo.framework.security.filter.strategy.success;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;

/**
 * Implementación que se encarga de validar la sesión existente
 */
public class InvalidateSessionStrategy implements SuccessStrategy {

    @Override
    public void onSucess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        request.getSession().invalidate();
    }

}
