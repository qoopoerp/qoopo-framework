package net.qoopo.framework.security.authorization.manager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.matcher.PermissionAuthenticacionMatcher;
import net.qoopo.framework.security.authorization.AuthorizationResponse;
import net.qoopo.framework.security.authorization.AuthorizationResult;
import net.qoopo.framework.security.permission.GrantedPermission;

/**
 * Implementacion de AuhtorizationManager que se encarga de verificar si una
 * autorizacion tiene un permiso en especifico
 */
public class HasPermissionAuthorizationManager<T> implements AuthorizationManager<T> {

    private PermissionAuthenticacionMatcher matcher;

    public HasPermissionAuthorizationManager(List<GrantedPermission> permissions) {
        matcher = new PermissionAuthenticacionMatcher(permissions);
    }

    public HasPermissionAuthorizationManager(GrantedPermission... permissions) {
        this(Arrays.asList(permissions));
    }

    @Override
    public AuthorizationResult authorize(Authentication authentication, T object) {
        boolean granted = matcher.matches(authentication);
        return new AuthorizationResponse(granted);
    }

}
