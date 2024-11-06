package net.qoopo.framework.security.authorization;

import net.qoopo.framework.security.authentication.Authentication;

public interface AuthorizationManager {

    public boolean isAllowed(Authentication authentication, String page);

    public boolean isAllowed(Authentication authentication, String page, String params);

    public boolean isAllowed(Authentication authentication, String page, String params, boolean abrir);
}
