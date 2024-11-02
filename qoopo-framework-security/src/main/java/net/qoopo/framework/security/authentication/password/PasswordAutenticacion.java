package net.qoopo.framework.security.authentication.password;

import java.security.Permission;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.CredentialsContainer;

@Getter
@Setter
public class PasswordAutenticacion implements Authentication, CredentialsContainer {

    private Collection<Permission> permissions;

    private boolean authenticated;

    private String password;

    public PasswordAutenticacion(String password) {
        this.password = password;
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
    public Collection<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public void eraseCredentials() {
        password=null;
    }

    @Override
    public Object getPrincipal() {
        return password;
    }

}
