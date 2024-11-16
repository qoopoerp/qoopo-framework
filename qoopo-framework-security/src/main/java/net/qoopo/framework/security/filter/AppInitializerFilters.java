package net.qoopo.framework.security.filter;

import java.util.logging.Logger;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.filter.authentication.BasicHttpAuthenticationFilter;
import net.qoopo.framework.security.filter.authentication.TokenAuthenticationFilter;
import net.qoopo.framework.security.filter.authentication.UserPasswordFormAuthenticationFilter;

@WebListener
public class AppInitializerFilters implements ServletContextListener {

        public static final Logger log = Logger.getLogger("AppInitializerFilters");

        @Override
        public void contextInitialized(ServletContextEvent sce) {
                if (SecurityConfig.get().isDebug())
                        log.info("[+] AppInitializerFilters- Configurando AppInitializerFilters");

                ServletContext servletContext = sce.getServletContext();
                // se aplican en el orden inverso en el que se agregan

                FilterRegistration.Dynamic authorizationFilter = servletContext.addFilter(
                                "filter_2_authorizationFilter",
                                new AuthorizationFilter());
                authorizationFilter.addMappingForUrlPatterns(null, false, "/*");

                FilterRegistration.Dynamic logoutFilter = servletContext.addFilter("filter_3_logoutFilter",
                                new LogoutFilter());
                logoutFilter.addMappingForUrlPatterns(null, false, "/*");

                FilterRegistration.Dynamic userPasswordFormFilter = servletContext.addFilter(
                                "filter_5_userPasswordFormFilter",
                                new UserPasswordFormAuthenticationFilter());
                userPasswordFormFilter.addMappingForUrlPatterns(null, false, "/*");

                FilterRegistration.Dynamic basicHttpFilter = servletContext.addFilter(
                                "filter_4_BasicAuthenticationFilter",
                                new BasicHttpAuthenticationFilter());
                basicHttpFilter.addMappingForUrlPatterns(null, false, "/*");

                FilterRegistration.Dynamic tokenAuthenticationFilter = servletContext.addFilter(
                                "TokenAuthenticationFilter",
                                new TokenAuthenticationFilter());
                tokenAuthenticationFilter.addMappingForUrlPatterns(null, false, "/*");

                FilterRegistration.Dynamic filtro1 = servletContext.addFilter(
                                "filter_1_securitycontextPersistenceFilter",
                                new SecurityContextPersistenceFilter());
                filtro1.addMappingForUrlPatterns(null, false, "/*");
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
                if (SecurityConfig.get().isDebug())
                        log.info("[+] AppInitializerFilters - Contexto finalizado ");
        }
}