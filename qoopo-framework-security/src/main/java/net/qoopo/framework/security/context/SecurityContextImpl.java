package net.qoopo.framework.security.context;

import net.qoopo.framework.security.authentication.Authentication;

public class SecurityContextImpl implements SecurityContext {

    private Authentication authentication;

    public SecurityContextImpl() {

    }

    public SecurityContextImpl(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

}
