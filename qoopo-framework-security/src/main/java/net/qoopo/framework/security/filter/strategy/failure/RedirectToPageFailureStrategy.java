package net.qoopo.framework.security.filter.strategy.failure;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RedirectToPageFailureStrategy implements FailureStrategy {

    private String pageToRedirect;

    public RedirectToPageFailureStrategy(String pageToRedirect) {
        this.pageToRedirect = pageToRedirect;
    }

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            net.qoopo.framework.security.exception.SecurityException exception)
            throws ServletException, IOException {
        // response.sendRedirect(request.getContextPath() +pageToRedirect);
        response.sendRedirect(pageToRedirect);

    }

}
