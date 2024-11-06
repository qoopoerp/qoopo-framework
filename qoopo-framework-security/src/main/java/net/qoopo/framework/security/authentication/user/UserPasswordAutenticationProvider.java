package net.qoopo.framework.security.authentication.user;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.password.BadCredentialsException;
import net.qoopo.framework.security.authentication.password.CredentialsExpiredException;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.authentication.service.UserService;

/**
 * Controlador default que implementa la autenticación
 */
@Getter
@Setter
public class UserPasswordAutenticationProvider implements AuthenticationProvider {

    private UserService userService;

    private UserData userData;

    public UserPasswordAutenticationProvider(UserService userService) {
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
                if (userData.isAccountLocked())
                    throw new UserLockedException("User locked");

                if (userData.isAccountExpired())
                    throw new UserExpiredException("User expired");

                if (userData.isCredentialsExpired())
                    throw new CredentialsExpiredException("Credentials expired");

                if (!userData.isEnabled())
                    throw new UserDisabledException("User disabled");

                if (userService.getPasswordEncoder().validate(userPasswordAutenticacion.getPassword(),
                        userData.getEncodedPassword())) {
                    authentication.setAuthenticated(true);
                    return authentication;
                } else {
                    throw new BadCredentialsException("Bad Credentials");
                }
            } else {
                // throw new AccessDeniedException("Access Denied");
                throw new BadCredentialsException("Bad Credentials");
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
