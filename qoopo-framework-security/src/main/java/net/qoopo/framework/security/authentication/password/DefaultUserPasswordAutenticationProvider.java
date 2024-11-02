package net.qoopo.framework.security.authentication.password;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.AccessDeniedException;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.service.UserService;
import net.qoopo.framework.security.authentication.user.UserData;

/**
 * Controlador default que implementa la autenticación
 */
@Getter
@Setter
public class DefaultUserPasswordAutenticationProvider implements AuthenticationProvider {

    private UserService userService;

    private UserData userData;

    public DefaultUserPasswordAutenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass()))
            return null;
        if (authentication instanceof UserPasswordAutenticacion) {
            UserPasswordAutenticacion userPasswordAutenticacion = (UserPasswordAutenticacion) authentication;
            userData = userService.findUserDataByUserName(userPasswordAutenticacion.getUser());
            if (userData != null) {
                if (userService.getPasswordEncoder().validate(userPasswordAutenticacion.getPassword(),
                        userData.getEncodedPassword())) {
                    authentication.setAuthenticated(true);
                    return authentication;
                } else {
                    throw new BadCredentialsException("Bad Credentials");
                }
            } else {
                throw new AccessDeniedException("Access Denied");
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.getName().equals(UserPasswordAutenticacion.class.getName()))
            return true;

        return false;
    }

}
