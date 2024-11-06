package net.qoopo.framework.security.filter.strategy.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Envía un error
 */
@NoArgsConstructor
@AllArgsConstructor
public class SendErrorForbiddenStrategy implements FailureStrategy {
    private String message = "No tiene acceso a esta pagina";

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            net.qoopo.framework.security.exception.SecurityException exception)
            throws ServletException, IOException {
        if (exception == null)
            response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
        else
            response.sendError(HttpServletResponse.SC_FORBIDDEN, message + " - " + exception.getMessage());
    }

}
