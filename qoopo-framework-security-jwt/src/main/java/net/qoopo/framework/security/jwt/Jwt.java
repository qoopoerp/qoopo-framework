package net.qoopo.framework.security.jwt;

import lombok.AllArgsConstructor;
import net.qoopo.framework.security.core.token.Token;

@AllArgsConstructor
public class Jwt implements Token {
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    public static Token of(String jwt) {
        return new Jwt(jwt);
    }
}
