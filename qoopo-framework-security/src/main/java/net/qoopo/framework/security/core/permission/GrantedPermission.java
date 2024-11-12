package net.qoopo.framework.security.core.permission;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * Representa un permiso consedido
 * 
 * Ejemplo: Escritura, Lectura, Acceso
 */
public interface GrantedPermission extends Serializable {
    public String getPermission();

    public static Collection<GrantedPermission> empty = Collections.EMPTY_LIST;

    public static boolean contains(Collection<GrantedPermission> permissions, GrantedPermission permission) {
        if (permissions != null && permission != null) {
            return permissions.stream().anyMatch(c -> c.getPermission().equals(permission.getPermission()));
        }
        return false;
    }
}
