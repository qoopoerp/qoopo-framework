package net.qoopo.framework.security.authentication.matcher;

import net.qoopo.framework.security.authentication.Authentication;

/**
 * Realiza una comparacacion de una autheticacion
 */
public interface AuthenticationMatcher {

    public boolean matches(Authentication authentication);

}
