package net.qoopo.framework.security.authentication.matcher;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.core.permission.DefaultGrantedPermission;
import net.qoopo.framework.security.core.permission.GrantedPermission;

/**
 * Requiere una autenticacion existente
 */
public class HasRoleMatcher implements AuthenticationMatcher {

    private static Logger log=Logger.getLogger("hasrolematcher");
    private List<GrantedPermission> roles;

    public HasRoleMatcher(String... roles) {
        this.roles = DefaultGrantedPermission.of(roles);
    }

    public HasRoleMatcher(GrantedPermission... roles) {
        this(Arrays.asList(roles));
    }

    public HasRoleMatcher(List<GrantedPermission> roles) {
        this.roles = roles;
    }

    @Override
    public boolean matches(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedPermission permission : roles) {
                // log.info("validando rol -> " + permission.getPermission() + " tiene? "+GrantedPermission.contains(authentication.getPermissions(), permission));
                if (!GrantedPermission.contains(authentication.getPermissions(), permission))
                    return false;
            }
        } else {
            // log.info("No autenticado ");
            return false;
        }
        return true;
    }

}
