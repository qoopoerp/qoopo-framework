package net.qoopo.framework.security.authentication.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Envía un error
 */
public class SendErrorForbiddenStrategy implements AuthenticationFailureStrategy {

    @Override
    public void onFailureAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {        
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene acceso a esta pagina");
    }

}
