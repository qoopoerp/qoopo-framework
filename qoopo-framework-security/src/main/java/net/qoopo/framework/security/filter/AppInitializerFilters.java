package net.qoopo.framework.security.filter;

import java.util.logging.Logger;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import net.qoopo.framework.security.filter.authentication.UserPasswordAuthenticationFilter;

@WebListener
public class AppInitializerFilters implements ServletContextListener {

    public static final Logger log = Logger.getLogger("AppInitializerFilters");

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        log.info("[+] AppInitializerFilters- Configurando AppInitializerFilters");

        ServletContext servletContext = sce.getServletContext();
        // se aplican en el orden inverso en el que se agregan

        FilterRegistration.Dynamic filtro4 = servletContext.addFilter("filter_4_userPasswordFilter",
                new UserPasswordAuthenticationFilter());
        filtro4.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic filtro3 = servletContext.addFilter("filter_3_logoutFilter",
                new LogoutFilter());
        filtro3.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic filtro2 = servletContext.addFilter("filter_2_authorizationFilter",
                new AuthorizationFilter());
        filtro2.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic filtro1 = servletContext.addFilter("filter_1_securitycontextPersistenceFilter",
                new SecurityContextPersistenceFilter());
        filtro1.addMappingForUrlPatterns(null, false, "/*");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("[+] AppInitializerFilters - Contexto finalizado ");
    }
}