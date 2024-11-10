package net.qoopo.framework.security.authentication.password;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.CredentialsContainer;
import net.qoopo.framework.security.permission.GrantedPermission;

@Getter
@Setter
public class PasswordAutenticacion implements Authentication, CredentialsContainer {

    private Collection<GrantedPermission> permissions;

    private boolean authenticated;

    private Object credentials;

    public PasswordAutenticacion(String password) {
        this.credentials = password;
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
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

}
