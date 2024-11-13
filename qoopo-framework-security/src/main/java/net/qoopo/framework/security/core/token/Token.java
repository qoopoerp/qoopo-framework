package net.qoopo.framework.security.core.token;

import java.util.Collection;

import net.qoopo.framework.security.core.permission.GrantedPermission;

public interface Token {
    public String getToken();

    public Object getPrincipal();

    public Collection<GrantedPermission> getPermissions();
}
