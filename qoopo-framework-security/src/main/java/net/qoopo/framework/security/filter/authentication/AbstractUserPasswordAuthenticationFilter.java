package net.qoopo.framework.security.filter.authentication;

import java.util.List;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.reflection.QoopoReflection;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.manager.ProviderManager;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.repository.InMemoryUserRepository;
import net.qoopo.framework.security.authentication.repository.RandomUserRepository;
import net.qoopo.framework.security.authentication.repository.UserRepository;
import net.qoopo.framework.security.authentication.user.DefaultUserService;
import net.qoopo.framework.security.authentication.user.UserPasswordAutenticacion;
import net.qoopo.framework.security.authentication.user.UserService;
import net.qoopo.framework.security.authentication.user.provider.UserPasswordAutenticationProvider;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.filter.AbstractAuthenticationProcessingFilter;

/**
 * filtro para realizar la autenticacion en el formato user password
 *
 * @author alberto
 */
public abstract class AbstractUserPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger log = Logger.getLogger("userpassword filter");

    protected boolean onlyPost = true;

    public AbstractUserPasswordAuthenticationFilter(String name) {
        super(name);
    }

    public AbstractUserPasswordAuthenticationFilter(String name, boolean onlyPost) {
        super(name);
        this.onlyPost = onlyPost;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (this.onlyPost && !request.getMethod().equals("POST")) {
            log.warning("[!] No Post Method [" + name + "]");
            // throw new AuthenticationServiceException("Authentication method not
            // supported: " + request.getMethod());
            // log.info("not post method");
            return null;
        }
        if (SecurityConfig.get().isDebug())
            log.info("[*] Attempting Auhenticacion [" + name + "]");

        String username = obtainUsername(request);
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            UserPasswordAutenticacion authRequest = UserPasswordAutenticacion.unauthenticated(username, password);
            return super.authenticationManager.authenticate(authRequest);
        } else
            return null;
    }

    protected abstract String obtainUsername(HttpServletRequest request);

    protected abstract String obtainPassword(HttpServletRequest request);

    /**
     * Sobrecarga la configuracion para setear el matcher del request
     */
    public void loadConfig() {
        super.loadConfig();
        if (enabled) {
            // configura el provider para el manager de autenticacion
            if (authenticationManager != null && authenticationManager instanceof ProviderManager) {
                ProviderManager providerManager = (ProviderManager) authenticationManager;
                if (isRequireAddUserProvider(providerManager)) {
                    UserService userService = getUserService();
                    if (userService != null) {
                        if (SecurityConfig.get().isDebug())
                            log.info("[+] UserService cargado -> " + userService.getClass().getCanonicalName());
                        providerManager.addAuthenticationProvider(new UserPasswordAutenticationProvider(userService));
                    } else {
                        UserRepository userRepository = getUserRepository();
                        if (SecurityConfig.get().isDebug())
                            log.info("[+] UserRepository Cargado? -> [" + (userRepository != null) + "] -  "
                                    + (userRepository != null ? userRepository.getClass().getCanonicalName() : ""));
                        if (userRepository != null) {
                            providerManager.addAuthenticationProvider(new UserPasswordAutenticationProvider(
                                    new DefaultUserService(
                                            userRepository, SecurityConfig.get().getPasswordEncoder())));
                        } else {
                            log.severe("[X] No se encuentra una implementación de UserRepository");
                        }
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
     * Busca una implementacion de un UserService que no sea las predeterminada
     * del framework,
     * 
     */
    private UserService getUserService() {
        List<UserService> implementations = QoopoReflection.getBeanImplemented(UserService.class,
                List.of(DefaultUserService.class));
        UserService userService = null;
        if (implementations != null && !implementations.isEmpty()) {
            if (SecurityConfig.get().isDebug())
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
            if (SecurityConfig.get().isDebug())
                log.info("[+] Repositorios encontrados: " + implementations.size());
            userRepository = implementations.get(0);
        }
        if (userRepository == null) {
            log.warning("[+] No se encontró repositorios externos, se toma el primero");
            userRepository = new RandomUserRepository();
            // userRepository=implementations.get(0);
        }
        return userRepository;
    }

}
