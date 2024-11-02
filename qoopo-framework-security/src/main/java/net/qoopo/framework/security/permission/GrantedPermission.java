package net.qoopo.framework.security.permission;

import java.io.Serializable;

/**
 * Representa un permiso consedido
 * 
 * Ejemplo: Escritura, Lectura, Acceso
 */
public interface GrantedPermission extends Serializable {
    public String getPermission();
}
