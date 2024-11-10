package net.qoopo.framework.security.authentication.user;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.CredentialsContainer;
import net.qoopo.framework.security.permission.GrantedPermission;

@Getter
@Setter
@ToString
public class UserPasswordAutenticacion implements Authentication, CredentialsContainer {

    private Collection<GrantedPermission> permissions;
    private boolean authenticated;
    private Object principal;
    private Object credentials;
    private Object details;

    private UserPasswordAutenticacion(Object principal, Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = false;
    }

    private UserPasswordAutenticacion(Object principal, Object credentials, Collection<GrantedPermission> permissions) {
        this.principal = principal;
        this.credentials = credentials;
        this.permissions = permissions;
        this.authenticated = true;
    }

    public static UserPasswordAutenticacion authenticated(Object principal, Object credentials,
            Collection<GrantedPermission> permissions) {
        return new UserPasswordAutenticacion(principal, credentials, permissions);
    }


    public static UserPasswordAutenticacion authenticated(Object principal, Object credentials,
            GrantedPermission permission) {
        return new UserPasswordAutenticacion(principal, credentials, List.of(permission));
    }

    public static UserPasswordAutenticacion unauthenticated(Object principal, Object credentials) {
        return new UserPasswordAutenticacion(principal, credentials);
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    @Override
    public Collection<GrantedPermission> getPermissions() {
        return permissions;
    }

    @Override
    public void eraseCredentials() {
        credentials = null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

}
