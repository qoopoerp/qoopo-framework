package net.qoopo.framework.security.authentication;

import java.util.Collection;
import java.io.Serializable;
import net.qoopo.framework.security.permission.GrantedPermission;

/**
 * Represneta una Autenticación.
 * 
 * Cuando un proveedor de autenticación (como Un validador de creenciales con un
 * repositorio e base de datos de usuario y Password)
 * realiza una autenticación devuelve un Autenticación que contiene el token de
 * la autenticación realizada
 */
public interface Authentication extends Serializable{

    public Object getPrincipal();

    public Object getCredentials();

    public Object getDetails();

    public boolean isAuthenticated();

    public void setAuthenticated(boolean isAuthenticated);

    public Collection<GrantedPermission> getPermissions();
}
