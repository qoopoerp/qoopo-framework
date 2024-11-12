package net.qoopo.framework.security.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This servlet filter protects the {@code x-frame-options/protected.jsp} page
 * against clickjacking by adding the {@code X-Frame-Options} header to the
 * response. The {@code urlPatterns} should be far more wildcard in a real web
 * application than in this demo project.
 *
 * @author Dominik Schadow
 */
@WebFilter(filterName = "filter_1_XFrameOptionsFilter", urlPatterns = { "/*" })
public class XFrameOptionsFilter
        implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader("X-Frame-Options", "SAMEORIGIN"); //Otras opcione DENY,  ALLOW-FROM http://localhost:8080
        response.addHeader("strict-transport-security", "max-age=16000000; includeSubDomains; preload;");
        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
