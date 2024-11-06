package net.qoopo.framework.security.authentication.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.AuthenticationException;
import net.qoopo.framework.security.authentication.CredentialsContainer;
import net.qoopo.framework.security.authentication.provider.AuthenticationProvider;

/**
 * Implementación predeterminada de AuthenticationManager en la cual se le
 * otorga una lista de providers que puede usar para realizar la autenticacion
 */
@Getter
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
        Authentication resultAuthentication = null;
        AuthenticationException lastException = null;
        for (AuthenticationProvider provider : providers) {
            if (!provider.supports(authentication.getClass()))
                continue;
            try {
                resultAuthentication = provider.authenticate(authentication);
            } catch (AuthenticationException e) {
                lastException = e;
            }
        }

        if (resultAuthentication != null) {
            if (resultAuthentication instanceof CredentialsContainer)
                ((CredentialsContainer) resultAuthentication).eraseCredentials();
            if (authentication instanceof CredentialsContainer)
                ((CredentialsContainer) authentication).eraseCredentials();

            // SecurityContext context = SecurityContextHolder.createEmptyContext();
            // context.setAuthentication(resultAuthentication);
            // SecurityContextHolder.setContext(context);
            return resultAuthentication;
        }

        if (lastException == null) {
            lastException = new NoProviderException("No provider for " + authentication.getClass());
        }

        throw lastException;

    }

    public void addAuthenticationProvider(AuthenticationProvider provider) {
        if (providers == null)
            providers = new ArrayList<>();
        this.providers.add(provider);
    }

}
