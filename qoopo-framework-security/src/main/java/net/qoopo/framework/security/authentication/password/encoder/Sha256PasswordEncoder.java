package net.qoopo.framework.security.authentication.password.encoder;

import net.qoopo.framework.crypt.Hash;

public class Sha256PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        return Hash.SHA256(password);
    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        return Hash.SHA256(clearPassword).equalsIgnoreCase(encodedPassword);
    }

    // @Override
    // public String getPasswordEncoded() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getPasswordEncoded'");
    // }

}
