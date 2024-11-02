package net.qoopo.framework.security.authentication.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RedirectToPageFailureStrategy implements AuthenticationFailureStrategy {

    private String pageToRedirect;

    public RedirectToPageFailureStrategy(String pageToRedirect) {
        this.pageToRedirect = pageToRedirect;
    }

    @Override
    public void onFailureAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // response.sendRedirect(request.getContextPath() +pageToRedirect);
        response.sendRedirect(pageToRedirect);

    }

}
