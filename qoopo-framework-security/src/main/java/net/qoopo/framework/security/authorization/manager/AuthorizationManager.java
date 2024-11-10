package net.qoopo.framework.security.authorization.manager;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authorization.AuthorizationDeniedException;
import net.qoopo.framework.security.authorization.AuthorizationResult;

/**
 * Un AuthorizationManager se encargar de verificar si una Authentication tiene
 * acceso a un objeto especifico
 * 
 * @þaram <T> el tipo del objeto que se va a comprobar el acceso
 */
public interface AuthorizationManager<T> {

    default public void verify(Authentication authentication, T object) {
        AuthorizationResult decision = authorize(authentication, object);
        if (decision != null && !decision.isGranted()) {
            throw new AuthorizationDeniedException("Access Denied");
        }
    }

    public AuthorizationResult authorize(Authentication authentication, T object);

}
