package net.qoopo.framework.security.authentication.user.provider;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.service.UserService;
import net.qoopo.framework.security.authentication.user.UserData;

/**
 * Provedor de autenticacion para usuario y password que usa un UserService
 */
@Getter
@Setter
public class UserPasswordAutenticationProvider extends AbstractUserPasswordAutenticationProvider {

    private UserService userService;

    public UserPasswordAutenticationProvider(UserService userService) {
        this.userService = userService;
    }

    protected UserData getUserData(String userName) {
        return userService.findUserDataByUserName(userName);
    }

    @Override
    protected boolean validateCredentials(String password, String encodedPassword) {
        return userService.getPasswordEncoder().validate(password, encodedPassword);
    }

}
