package net.qoopo.framework.security.filter.authentication;

import java.util.List;
import java.util.logging.Logger;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.reflection.QoopoReflection;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.manager.ProviderManager;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.repository.InMemoryUserRepository;
import net.qoopo.framework.security.authentication.repository.RandomUserRepository;
import net.qoopo.framework.security.authentication.repository.UserRepository;
import net.qoopo.framework.security.authentication.service.DefaultUserService;
import net.qoopo.framework.security.authentication.service.UserService;
import net.qoopo.framework.security.authentication.user.UserPasswordAutenticacion;
import net.qoopo.framework.security.authentication.user.provider.UserPasswordAutenticationProvider;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.filter.AbstractAuthenticationProcessingFilter;
import net.qoopo.framework.security.matcher.UrlRequestMatcher;

/**
 * Filtro que intentar autenticar cuando hay un formulario de usuario y password
 *
 * @author alberto
 */
// @WebFilter(filterName = "filter_4_userPasswordFilter", urlPatterns = { "/*"
// })
public class UserPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger log = Logger.getLogger("userpassword filter");

    private boolean onlyPost = true;

    public UserPasswordAuthenticationFilter() {
        super("userPasswordFilter");
    }

    public UserPasswordAuthenticationFilter(boolean onlyPost) {
        super("userPasswordFilter");
        this.onlyPost = onlyPost;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (this.onlyPost && !request.getMethod().equals("POST")) {
            // throw new AuthenticationServiceException("Authentication method not
            // supported: " + request.getMethod());
            // log.info("not post method");
            return null;
        }

        log.info("[**] attemptAuhenticacion");
        String username = obtainUsername(request);
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            log.info("[**] intentando con  " + username + " / " + password);
            UserPasswordAutenticacion authRequest = UserPasswordAutenticacion.unauthenticated(username, password);
            return super.authenticationManager.authenticate(authRequest);
        } else
            return null;
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
        super.loadConfig();
        if (requiresAuthenticationRequestMatcher == null) {
            // si no encuentra una LoginPage usa la default del framework
            if (SecurityConfig.get().getLoginConfigurer().getLoginPage() == null) {
                SecurityConfig.get().login(login -> login.defaults());
            }
            // solo realiza la authenticación cuando la solicitud tenga la url de
            // authenticacion
            requiresAuthenticationRequestMatcher = new UrlRequestMatcher(
                    SecurityConfig.get().getLoginConfigurer().getLoginPage(),
                    onlyPost ? "POST" : null);
        }

        // configura el provider para el manager de autenticacion
        if (authenticationManager != null && authenticationManager instanceof ProviderManager) {
            ProviderManager providerManager = (ProviderManager) authenticationManager;
            if (isRequireAddUserProvider(providerManager)) {
                UserService userService = getUserService();
                if (userService != null) {
                    log.info("[+] UserService cargado -> " + userService.getClass().getCanonicalName());
                    providerManager.addAuthenticationProvider(new UserPasswordAutenticationProvider(userService));
                } else {
                    UserRepository userRepository = getUserRepository();
                    log.info("[+] UserRepository Cargado? -> [" + (userRepository != null) + "] -  "
                            + (userRepository != null ? userRepository.getClass().getCanonicalName() : ""));
                    if (userRepository != null) {
                        providerManager.addAuthenticationProvider(new UserPasswordAutenticationProvider(
                                new DefaultUserService(
                                        userRepository, SecurityConfig.get().getPasswordEncoder())));
                    } else {
                        log.severe("No se encuentra una implementación de UserRepository");
                    }
                }
            }
        }
    }

    /**
     * Valida si es necesario agregar el UserPasswordAuthenticationProvider al
     * authenticationManager
     * 
     * @param providerManager
     * @return
     */
    private boolean isRequireAddUserProvider(ProviderManager providerManager) {
        boolean addProvider = providerManager.getProviders() == null || providerManager.getProviders().isEmpty();
        if (!addProvider && providerManager.getProviders() != null) {
            addProvider = true;
            for (AuthenticationProvider provider : providerManager.getProviders()) {
                if (provider instanceof UserPasswordAutenticationProvider) {
                    addProvider = false;
                }
            }
        }
        return addProvider;
    }

    /**
     * Busca una implementacion de un UserService que no se las predeterminada
     * del framework,
     * En caso de no encontrar usara la predeterminada
     */
    private UserService getUserService() {
        List<UserService> implementations = QoopoReflection.getBeanImplemented(UserService.class,
                List.of(DefaultUserService.class));
        UserService userService = null;
        if (implementations != null && !implementations.isEmpty()) {
            log.info("[+] UserService encontrados: " + implementations.size());
            userService = implementations.get(0);
        }
        return userService;
    }

    /**
     * Busca una implementacion de un UserRepository que no se las predeterminadas
     * del framework,
     * En caso de no encontrar usara una de generación aleatoria
     */
    private UserRepository getUserRepository() {
        List<UserRepository> implementations = QoopoReflection.getBeanImplemented(UserRepository.class,
                List.of(RandomUserRepository.class, InMemoryUserRepository.class));
        UserRepository userRepository = null;
        if (implementations != null && !implementations.isEmpty()) {
            log.info("[+] Repositorios encontrados: " + implementations.size());
            userRepository = implementations.get(0);
        }
        if (userRepository == null) {
            log.info("[+] No se encontró repositorios externos, se toma el primero");
            userRepository = new RandomUserRepository();
            // userRepository=implementations.get(0);
        }
        return userRepository;
    }

}
