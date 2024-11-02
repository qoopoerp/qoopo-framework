package net.qoopo.framework.security.authentication.manager;

import java.util.Arrays;
import java.util.List;

import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.CredentialsContainer;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;
import net.qoopo.framework.security.context.SecurityContextHolder;

/**
 * Implementación predeterminada de AuthenticationManager en la cual se le
 * otorga una lista de providers que puede usar para realizar la autenticacion
 */
public class ProviderManager implements AuthenticationManager {

    private List<AuthenticationProvider> providers;

    public ProviderManager(AuthenticationProvider... providers) {
        this(Arrays.asList(providers));
    }

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication resulAuthentication = null;
        AuthenticationException lastException = null;
        for (AuthenticationProvider provider : providers) {
            if (!provider.supports(authentication.getClass()))
                continue;
            try {
                resulAuthentication = provider.authenticate(authentication);
            } catch (AuthenticationException e) {
                lastException = e;
            }
        }

        if (resulAuthentication != null) {
            if (resulAuthentication instanceof CredentialsContainer)
                ((CredentialsContainer) resulAuthentication).eraseCredentials();
            if (authentication instanceof CredentialsContainer)
                ((CredentialsContainer) authentication).eraseCredentials();

            SecurityContextHolder.getContext().setAuthentication(resulAuthentication);
            
            return resulAuthentication;
        }

        if (lastException == null) {
            lastException = new NoProviderException("No provider for " + authentication.getClass());
        }

        throw lastException;

    }

}
