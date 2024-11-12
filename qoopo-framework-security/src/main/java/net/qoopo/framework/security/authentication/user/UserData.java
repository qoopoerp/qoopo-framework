package net.qoopo.framework.security.authentication.user;

import java.util.Collection;

import net.qoopo.framework.security.authentication.password.PasswordData;
import net.qoopo.framework.security.core.permission.GrantedPermission;

public interface UserData extends PasswordData {

    public String getUser();

    public Collection<GrantedPermission> getPermissions();

    /**
     * Indica que la cuenta de usuario ha expirado. Una cuenta expirada no puede ser
     * autenticada
     * 
     * @return true si la cuenta está expirada, false si no está expirada
     */
    default boolean isAccountExpired() {
        return false;
    }

    /**
     * Indica si la cuenta de usuario está bloqueada. Si está bloqueada no puede ser
     * autenticada
     * 
     * @return true si está bloqueada, false si no
     */
    default boolean isAccountLocked() {
        return true;
    }

    /**
     * Indica si el usuairo está habilitado. Un usuario no habilitado no puede ser
     * autenticado
     * 
     * @return
     */
    default boolean isEnabled() {
        return true;
    }

}
