package net.qoopo.framework.security.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.qoopo.framework.security.core.token.TokenProvider;

/**
 * Configurador de provedores de Tokens
 */
@Getter
public class TokensProvidersConfigurer {
    private List<TokenProvider> providers = new ArrayList<>();

    public TokensProvidersConfigurer add(TokenProvider provider) {
        this.providers.add(provider);
        return this;
    }

}
