package net.qoopo.framework.security.jwt;

import java.util.Collection;

import lombok.AllArgsConstructor;
import net.qoopo.framework.security.core.permission.GrantedPermission;
import net.qoopo.framework.security.core.token.Token;

@AllArgsConstructor
public class Jwt implements Token {
    private String token;

    private Object principal;
    private Collection<GrantedPermission> permissions;

    @Override
    public String getToken() {
        return token;
    }

    public static Token of(String jwt, Object principal, Collection<GrantedPermission> permissions) {
        return new Jwt(jwt, principal, permissions);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Collection<GrantedPermission> getPermissions() {
        return permissions;
    }
}
