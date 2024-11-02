package net.qoopo.framework.security.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
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
import net.qoopo.framework.security.SecurityConfig;
import net.qoopo.framework.security.authentication.failure.AuthenticationFailureStrategy;
import net.qoopo.framework.security.authentication.failure.RedirectToLoginFailureStrategy;
import net.qoopo.framework.security.matcher.RequestMatcher;
import net.qoopo.framework.security.matcher.RoutesDontRequiresAuthenticacionMatcher;
import net.qoopo.framework.security.matcher.RoutesRequiresAutenticationMatcher;

/**
 * Filter que valida el acceso a las rutas
 *
 * @author alberto
 */
@WebFilter(filterName = "securityFilter", urlPatterns = { "/lasjdlkajsdkajsdlajsdñlas*" })
public class SecurityFilter implements Filter {

    public static final Logger log = Logger.getLogger("Qoopo-router-filter");

    private static final boolean DEBUG = true;
    private FilterConfig filterConfig = null;

    private RequestMatcher requiresAuthenticationRequestMatcher = new RoutesRequiresAutenticationMatcher();

    private RequestMatcher dontRequiresAuthenticationRequestMatcher = new RoutesDontRequiresAuthenticacionMatcher();

    private AuthenticationFailureStrategy authenticationNotExistsStrategy = new RedirectToLoginFailureStrategy();

    // @Inject
    // private AuthenticationManager authenticationManager;

    // @Inject
    // private AuthorizationManager authorizationListener;

    // @Inject
    // private Authentication authentication;

    public SecurityFilter() {
        // Login constructor
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
            log.warning("SecurityConfig is disabled");
            return;
        }

        if (!requiresAuthentication(request, response)) {
            log.warning("route dont require authentication [" + request.getServletPath() + "]");
            chain.doFilter(request, response);
            return;
        }

        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // // en este punto solo pasa si se requiere authenticacion
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     // delegamos la respuesta a una implementacion de no autorizado, esto puede ser
        //     // solicitar credenciales o mensaje de error

        //     authenticationNotExistsStrategy.onFailureAuthentication(request, response);
        //     return;

        // }

        // ahora se valida si está autorizado para el recurso solicitado

        // String pagina = request.getServletPath();
        // if (!authorizationListener.isAllowed(authentication, pagina)) {
        // log.severe("[!] Se deniega acceso a la ruta " + pagina);
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene acceso a esta
        // pagina");
        // return;
        // }

        // String params = "";
        // for (String paramId : request.getParameterMap().keySet()) {
        // params += paramId + "=" + request.getParameter(paramId) + "&";
        // }
        // String urlStr = request.getRequestURI();

        // SecurityRoute route = SecurityRouter.validateRoute(urlStr.toLowerCase());

        // if (DEBUG) {
        // log.log(Level.INFO, "[**] {0} - tipo proteccion=[{1}]", new Object[] {
        // urlStr, route.toString() });
        // }

        // switch (route.getType()) {
        // // case SecurityRoute.TYPE_PUBLIC:
        // // // Si no requiere protección continúo normalmente.
        // // chain.doFilter(request, response);
        // // return;
        // case SecurityRoute.TYPE_BACKEND:
        // if (route.isRequireSession()
        // && (authentication == null || !authentication.isAuthenticated())) {
        // response.sendRedirect(request.getContextPath() +
        // SecurityConfig.get().getLoginPage() + "?pageTo="
        // + pagina + "&" + params);
        // return;
        // }

        // // en caso que si este autenticado, validamos si tiene permiso a esa ruta
        // if (route.isRequireSession()
        // && route.isRequirePermission()
        // // && authenticationManager != null
        // && authentication != null
        // && authentication.isAuthenticated()
        // // && !authorizationListener.isAllowed(authentication, pagina)
        // ) {
        // log.severe("[!] Se deniega acceso a la ruta " + pagina);
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene acceso a esta
        // pagina");
        // return;
        // }
        // break;
        // case SecurityRoute.TYPE_WEBSITE:
        // if (route.isRequireSession()
        // && (authentication == null || !authentication.isAuthenticated())) {
        // response.sendRedirect(request.getContextPath() +
        // SecurityConfig.get().getPublicPage());
        // return;
        // }

        // // en caso que si teste logueado, validamos si tiene permiso a esa ruta
        // if (route.isRequireSession() && route.isRequirePermission()
        // // && authenticationManager != null
        // && authentication != null
        // && authentication.isAuthenticated()
        // // && !authorizationListener.isAllowed(authentication, pagina)
        // ) {
        // log.severe("[!] Se deniega acceso a la ruta " + pagina);
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene acceso a esta
        // pagina");
        // return;
        // }
        // break;
        // case SecurityRoute.TYPE_INVALID:
        // default:
        // // if (DEBUG) {
        // log.log(Level.WARNING, "[!!!] Recurso no existe [{0}]", urlStr);
        // log.log(Level.WARNING, "[!!!] Reenviando a [{0}]",
        // SecurityConfig.get().getInvalidPage());
        // // }
        // // res.sendRedirect(req.getContextPath() + "/404.jsf");
        // response.sendRedirect(request.getContextPath() +
        // SecurityConfig.get().getInvalidPage());
        // break;
        // }

        // ---------------------------------------------------------------------------------------
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            problem = e;
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        // en este lugar se puede llamar a un metodo despues de filtro

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Comprueba si la solicitud requiere autenticacion
     * 
     * @param request
     * @param response
     * @return
     */
    private boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return !dontRequiresAuthenticationRequestMatcher.match(request);
    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
        // DEstroy
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null && DEBUG) {
            log.info("login:Initializing filter");
        }
    }

    /**
     * Return a String representation of this object.
     *
     * @return
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("login()");
        }
        StringBuilder sb = new StringBuilder("login(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                try (PrintStream ps = new PrintStream(response.getOutputStream());
                        PrintWriter pw = new PrintWriter(ps)) {
                    pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); // NOI18N
                    // PENDING! Localize this for next official release
                    pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                    pw.print(stackTrace);
                    pw.print("</pre></body>\n</html>"); // NOI18N
                }
                response.getOutputStream().close();
            } catch (IOException ex) {
                // Catch
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
            // catch
        }
        return stackTrace;
    }
}
