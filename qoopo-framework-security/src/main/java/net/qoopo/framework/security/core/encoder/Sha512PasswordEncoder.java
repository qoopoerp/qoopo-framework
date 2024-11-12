package net.qoopo.framework.security.core.encoder;

import net.qoopo.framework.crypt.Hash;

public class Sha512PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        return Hash.SHA512(password);
    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        return Hash.SHA512(clearPassword).equalsIgnoreCase(encodedPassword);
    }

}
