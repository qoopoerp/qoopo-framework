/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import net.qoopo.framework.security.config.SecurityConfig;

/**
 *
 * @author lukas
 */
// Enable it for Servlet 3.x implementations
@WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class CORSFilter implements Filter {

    private static Logger log = Logger.getLogger("CorsFilter");

    public CORSFilter() {

    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {

    }

    /**
     * @param servletRequest
     * @param servletResponse
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (SecurityConfig.get().isDebug()) {
            log.info("CORSFilter HTTP Request: [" + request.getMethod() + "] [" + request.getRequestURI() + "]");
        }

        // Authorize (allow) all domains to consume the content
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Credentials", "true");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "GET,  POST");
        // ((HttpServletResponse)
        // servletResponse).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS,
        // HEAD, PUT, POST");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers",
                "Content-Type, X-Amz-Date, X-Api-Key, Origin, Accept, Access-Control-Allow-Headers, Access-Control-Allow-Methods, Access-Control-Allow-Origin, origin, content-type, accept, authorization, Authorization");
        // Headers expose
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Expose-Headers", HttpHeaders.AUTHORIZATION);
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS
        // handshake
        // if (request.getMethod().equals("OPTIONS")) {
        // resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        // return;
        // }

        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        // pass the request along the filter chain
        chain.doFilter(request, servletResponse);
    }

    /**
     * @param fConfig
     * @throws javax.servlet.ServletException
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {

    }

}
