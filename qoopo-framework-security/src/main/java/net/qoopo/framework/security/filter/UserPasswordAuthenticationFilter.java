package net.qoopo.framework.security.filter;

import java.util.logging.Logger;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.password.UserPasswordAutenticacion;

/**
 * Filter que valida el acceso a las rutas
 *
 * @author alberto
 */
@WebFilter(filterName = "userPasswordFilter", urlPatterns = { "/*" })
public class UserPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger log = Logger.getLogger("userpassword filter");

    private boolean post = true;

    public UserPasswordAuthenticationFilter() {
        // Login constructor
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        if (this.post && !request.getMethod().equals("POST")) {
            // throw new AuthenticationServiceException("Authentication method not
            // supported: " + request.getMethod());
            log.info("not post method");
            return null;
        }

        log.info("[**] attemptAuhenticacion");
        String username = obtainUsername(request);
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            log.info("[**] intentando con  " + username + " / " + password);
            UserPasswordAutenticacion authRequest = new UserPasswordAutenticacion(username, password);
            return super.authenticationManager.authenticate(authRequest);
        } else
            return null;
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

}
