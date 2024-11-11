package net.qoopo.framework.security.filter.authentication;

import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.matcher.UrlRequestMatcher;

/**
 * Filtro que intentar autenticar cuando hay un formulario de usuario y password
 *
 * @author alberto
 */
public class UserPasswordFormAuthenticationFilter extends AbstractUserPasswordAuthenticationFilter {

    private static Logger log = Logger.getLogger("userpassword filter");

    public UserPasswordFormAuthenticationFilter() {
        super("userPasswordFormFilter");
    }

    public UserPasswordFormAuthenticationFilter(boolean onlyPost) {
        super("userPasswordFormFilter", onlyPost);
    }

    protected String obtainUsername(HttpServletRequest request) {
        String userName = request.getParameter("username");
        if (userName == null || userName.isEmpty())
            userName = request.getParameter("j_username");
        if (userName == null || userName.isEmpty())
            userName = request.getParameter("user");
        return userName;
    }

    protected String obtainPassword(HttpServletRequest request) {
        String password = request.getParameter("password");
        if (password == null || password.isEmpty())
            password = request.getParameter("j_password");
        return password;
    }

    /**
     * Sobrecarga la configuracion para setear el matcher del request
     */
    public void loadConfig() {
        if (requiresAuthenticationRequestMatcher == null) {
            // si no encuentra una LoginPage usa la default del framework si no se ha
            // indicado otro metodo como el basicHttp
            if (SecurityConfig.get().getLoginConfigurer().getLoginPage() == null
                    && !SecurityConfig.get().getLoginConfigurer().isConfigured()) {
                SecurityConfig.get().login(login -> login.defaults());
            }

            // solo realiza la authenticación cuando la solicitud tenga la url de
            // authenticacion
            if (SecurityConfig.get().getLoginConfigurer().getLoginPage() != null
            // && !SecurityConfig.get().getLoginConfigurer().isConfigured()
            ) {
                requiresAuthenticationRequestMatcher = new UrlRequestMatcher(
                        SecurityConfig.get().getLoginConfigurer().getLoginPage(),
                        super.onlyPost ? "POST" : null);
            } else {
                // deshabilita el filto
                enabled = false;
            }
        }

        super.loadConfig();
    }

}
