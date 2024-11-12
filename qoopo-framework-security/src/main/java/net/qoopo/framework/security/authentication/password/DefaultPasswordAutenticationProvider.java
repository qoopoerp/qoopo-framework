package net.qoopo.framework.security.authentication.password;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;

/**
 * Controlador default que implementa la autenticación
 */
@Getter
@Setter
public class DefaultPasswordAutenticationProvider implements AuthenticationProvider {

    private PasswordService passwordService;

    public DefaultPasswordAutenticationProvider(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass()))
            return null;
        if (authentication instanceof PasswordAutenticacion) {
            PasswordAutenticacion PasswordAutenticacion = (PasswordAutenticacion) authentication;
            PasswordData passwordData = passwordService.findPassword();
            if (passwordService.getPasswordEncoder().validate((String) PasswordAutenticacion.getCredentials(),
                    passwordData.getEncodedPassword())) {
                authentication.setAuthenticated(true);
                return authentication;
            } else {
                throw new BadCredentialsException("Bad Credentials");
            }

        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.getName().equals(PasswordAutenticacion.class.getName()))
            return true;

        return false;
    }

}
