package net.qoopo.framework.security.authentication.matcher;

import java.util.Arrays;
import java.util.List;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.permission.GrantedPermission;

public class PermissionAuthenticacionMatcher implements AuthenticationMatcher {

    private List<GrantedPermission> permissions;

    public PermissionAuthenticacionMatcher(List<GrantedPermission> permissions) {
        this.permissions = permissions;
    }

    public PermissionAuthenticacionMatcher(GrantedPermission... permissions) {
        this(Arrays.asList(permissions));
    }

    @Override
    public boolean matches(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated())
            return false;

        boolean matches = false;
        // verifica si tiene uno de los permisos necesarios
        for (GrantedPermission permission : permissions) {
            for (GrantedPermission permissionAythenticacion : authentication.getPermissions()) {
                if (permissionAythenticacion.getPermission().equals(permission.getPermission())) {
                    matches = true;
                }
            }
        }
        return matches;
    }

}
