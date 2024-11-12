package net.qoopo.framework.security.filter.strategy.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.config.SecurityConfig;

/**
 * Realiza un redirect a la pagina de login regisrada, agregando el parámetro
 * pageTo
 */
public class RedirectToLoginFailureStrategy implements FailureStrategy {

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            net.qoopo.framework.security.exception.SecurityException exception)
            throws ServletException, IOException {

        String pagina = request.getServletPath();
        String params = "";
        for (String paramId : request.getParameterMap().keySet()) {
            params += paramId + "=" + request.getParameter(paramId) + "&";
        }

        if (exception != null)
            response.sendRedirect(
                    request.getContextPath() + SecurityConfig.get().getLoginConfigurer().getLoginPage() + "?error="
                            + exception.getMessage()
                            //  + "&pageTo=" + pagina + "&" + params
                            );
        else
            response.sendRedirect(
                    request.getContextPath() + SecurityConfig.get().getLoginConfigurer().getLoginPage() 
                    // + "?pageTo="+ pagina + "&" + params
                    );

    }

}
