package net.qoopo.framework.security.core.token;

import net.qoopo.framework.security.authentication.user.UserData;

public interface TokenProvider {

    public Token generate(UserData userData);

    public boolean validate(String token);

    public Token getToken(String token);
}
