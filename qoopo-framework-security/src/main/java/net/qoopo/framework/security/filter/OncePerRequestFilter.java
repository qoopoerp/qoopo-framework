package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.config.SecurityConfig;

/**
 * Las implementaciones de OnePerRequestFilter solo se ejecutan una ver por
 * Request
 */
public abstract class OncePerRequestFilter extends AbstractSecurityFilter {

    private static Logger log = Logger.getLogger("once-per-request-security-filter");

    private String propertyName;



    public OncePerRequestFilter(String name) {
        super(name);
        this.propertyName = "FILTER_" + name + "_APPLIED";
    }

    /**
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     *
     * @exception IOException      if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (!SecurityConfig.get().isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        if (!enabled) {
            chain.doFilter(request, response);
            return;
        }

        // se asegura que el filtro se aplica una sola vez por request
        if (request.getAttribute(propertyName) != null) {
            if (SecurityConfig.get().isDebug())
                log.info("[!] Descartando filtro :" + name);
            chain.doFilter(request, response);
            return;
        }
        request.setAttribute(propertyName, Boolean.TRUE);
        if (SecurityConfig.get().isDebug())
            log.info("[+] Aplicando filtro único :" + name);
        doInternalFilter(request, response, chain);
    }

}
