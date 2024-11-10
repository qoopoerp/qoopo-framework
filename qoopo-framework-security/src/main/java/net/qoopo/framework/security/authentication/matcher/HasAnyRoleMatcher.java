package net.qoopo.framework.security.authentication.matcher;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.permission.DefaultGrantedPermission;
import net.qoopo.framework.security.permission.GrantedPermission;

/**
 * Requiere una autenticacion existente
 */
public class HasAnyRoleMatcher implements AuthenticationMatcher {

    private static Logger log = Logger.getLogger("has any role matcheer");
    private List<GrantedPermission> roles;

    public HasAnyRoleMatcher(String... roles) {
        this.roles = DefaultGrantedPermission.of(roles);
    }

    public HasAnyRoleMatcher(GrantedPermission... roles) {
        this(Arrays.asList(roles));
    }

    public HasAnyRoleMatcher(List<GrantedPermission> roles) {
        this.roles = roles;
    }

    @Override
    public boolean matches(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedPermission permission : roles) {
                log.info("validando rol -> " + permission.getPermission() + " tiene? "
                        + GrantedPermission.contains(authentication.getPermissions(), permission));
                if (GrantedPermission.contains(authentication.getPermissions(), permission))
                    return true;
            }
            log.info("no tiene cuale sosn los roles autenticados?");
            authentication.getPermissions().forEach(c -> log.info("rol-> " + c.getPermission()));
        }

        return false;
    }

}
