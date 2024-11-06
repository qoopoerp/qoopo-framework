package net.qoopo.framework.security.authentication.password.encoder;

import net.qoopo.framework.crypt.Hash;

public class DefaulPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        return Hash.HASH(password);
    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        return Hash.HASH(clearPassword).equalsIgnoreCase(encodedPassword);
    }

}
