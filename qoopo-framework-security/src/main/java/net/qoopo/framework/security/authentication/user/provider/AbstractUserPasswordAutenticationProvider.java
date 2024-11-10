package net.qoopo.framework.security.authentication.user.provider;

import java.util.logging.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.password.BadCredentialsException;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.authentication.user.UserPasswordAutenticacion;
import net.qoopo.framework.security.authentication.user.check.DefaultPostAuthenticationChecker;
import net.qoopo.framework.security.authentication.user.check.DefaultPreAuthenticacionChecker;
import net.qoopo.framework.security.authentication.user.check.UserDataChecker;

/**
 * Controlador default que implementa la autenticación
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractUserPasswordAutenticationProvider implements AuthenticationProvider {

    private static Logger log = Logger.getLogger("user-password-autentication-provider");

    private UserDataChecker preAuthenticationChecker = new DefaultPreAuthenticacionChecker();
    private UserDataChecker postAuthenticationChecker = new DefaultPostAuthenticationChecker();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass()))
            return null;

        if (authentication instanceof UserPasswordAutenticacion) {
            UserPasswordAutenticacion userPasswordAutenticacion = (UserPasswordAutenticacion) authentication;
            String userName = getUserName(userPasswordAutenticacion);
            UserData userData = getUserData(userName);
            if (userData != null) {
                preAuthenticationChecker.check(userData);
                if (validateCredentials(getPassword(authentication), userData.getEncodedPassword())) {
                    postAuthenticationChecker.check(userData);
                } else {
                    throw new BadCredentialsException("Bad Credentials");
                }
            } else {
                // throw new AccessDeniedException("Access Denied");
                throw new BadCredentialsException("Bad Credentials");
            }
            return createSuccessAuthentication(userName, userPasswordAutenticacion, userData);
        } else {
            log.warning("authentication is not UserPasswordAutenticacion ");
            return null;
        }
    }

    protected abstract boolean validateCredentials(String password, String encodedPassword);

    protected abstract UserData getUserData(String userName);

    protected String getUserName(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }

    protected String getPassword(Authentication authentication) {
        return (String) authentication.getCredentials();
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
            UserData user) {
        UserPasswordAutenticacion result = UserPasswordAutenticacion.authenticated(principal,
                authentication.getCredentials(), user.getPermissions());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.getName().equals(UserPasswordAutenticacion.class.getName()))
            return true;
        return false;
    }

}
