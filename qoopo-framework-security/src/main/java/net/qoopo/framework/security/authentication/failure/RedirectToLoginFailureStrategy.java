package net.qoopo.framework.security.authentication.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.SecurityConfig;

/**
 * Realiza un redirect a la pagina de login regisrada, agregando el parámetro pagoTo 
 */
public class RedirectToLoginFailureStrategy implements AuthenticationFailureStrategy {

    @Override
    public void onFailureAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String pagina = request.getServletPath();
        String params = "";
        for (String paramId : request.getParameterMap().keySet()) {
            params += paramId + "=" + request.getParameter(paramId) + "&";
        }

        response.sendRedirect(request.getContextPath() + SecurityConfig.get().getLoginPage() + "?pageTo="
                + pagina + "&" + params);

    }

}
