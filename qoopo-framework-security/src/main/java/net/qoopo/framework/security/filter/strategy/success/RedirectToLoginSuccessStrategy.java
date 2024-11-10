package net.qoopo.framework.security.filter.strategy.success;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.config.SecurityConfig;

public class RedirectToLoginSuccessStrategy implements SuccessStrategy {

    @Override
    public void onSucess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        response.sendRedirect(request.getContextPath() + SecurityConfig.get().getLoginConfigurer().getLoginPage());
    }

}
