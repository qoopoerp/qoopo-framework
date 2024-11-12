package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.core.context.SecurityContext;
import net.qoopo.framework.security.core.context.SecurityContextHolder;
import net.qoopo.framework.security.core.context.repository.SecurityContextHttpRepository;
import net.qoopo.framework.security.core.context.repository.SecurityContextRepository;
import net.qoopo.framework.security.web.repository.HttpWarehouse;

/**
 * Filtro que se encarga de gestionar el almacenamiento del seuritycontext en la
 * session para mantenerlo entre solicitudes
 */
// @WebFilter(filterName = "filter_1_securitycontextPersistenceFilter",
// urlPatterns = { "/*" })
public class SecurityContextPersistenceFilter extends OncePerRequestFilter {

    private static Logger log = Logger.getLogger("security-context-persistence-filter");

    private SecurityContextRepository<HttpWarehouse> repository = new SecurityContextHttpRepository(false);

    public SecurityContextPersistenceFilter() {
        super("securitycontextPersistenceFilter");
    }

    protected void doInternalFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (SecurityConfig.get().isDebug())
            log.info("[+] Leyendo SecurityContext");
        HttpWarehouse warehouse = HttpWarehouse.of(request, response);
        SecurityContext securityContextBefore = this.repository.load(warehouse);
        try {
            if (securityContextBefore != null) {
                if (SecurityConfig.get().isDebug())
                    log.info("[+] Se encontró security context en la session");
                SecurityContextHolder.setContext(securityContextBefore);
            } else {
                if (SecurityConfig.get().isDebug())
                    log.warning("[!] NO SE ENCONTRÓ UN SECURITY CONTEXT");
            }
            chain.doFilter(request, response);
        } finally {
            SecurityContext securityContextAfter = SecurityContextHolder.getContext();
            if (SecurityConfig.get().isDebug())
                if (securityContextAfter != null) {
                    log.info("[+] Despues del filtro se encontró un security context");
                } else {
                    log.warning("[!] NO SE ENCONTRÓ UN SECURITY CONTEXT AL FINAL DEL FILTRO");
                }

            this.repository.save(securityContextAfter, warehouse);
            // elimina para no dejar autenticado para las otras solicitudes
            // SecurityContextHolder.clear();
        }

    }

}
