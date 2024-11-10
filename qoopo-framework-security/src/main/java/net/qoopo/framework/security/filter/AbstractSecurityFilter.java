package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.config.SecurityConfig;

/**
 * Filtro abstracto que debe implementar todo filtro del framework security
 */
public abstract class AbstractSecurityFilter implements Filter {

    private static Logger log = Logger.getLogger("abstract-security-filter");

    protected String name;

    public AbstractSecurityFilter(String name) {
        this.name = name;
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
        log.info("[0] Aplicando filtro :" + name);
        doInternalFilter(request, response, chain);
    }

    protected abstract void doInternalFilter(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException;

}
