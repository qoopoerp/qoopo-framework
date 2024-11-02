package net.qoopo.framework.security.authentication.success;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;

public class RedirectToPageSuccessStrategy implements AuthenticationSuccessStrategy {

    private String pageToRedirect;

    public RedirectToPageSuccessStrategy(String pageToRedirect) {
        this.pageToRedirect = pageToRedirect;
    }

    @Override
    public void onSucessAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        // response.sendRedirect(request.getContextPath() +pageToRedirect);
        response.sendRedirect(pageToRedirect);

    }

}
