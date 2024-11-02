package net.qoopo.framework.security.authentication.password.encoder;

import net.qoopo.framework.crypt.Hash;

public class ShaPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        return Hash.SHA(password);
    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        return Hash.SHA(clearPassword).equalsIgnoreCase(encodedPassword);
    }

    // @Override
    // public String getPasswordEncoded() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getPasswordEncoded'");
    // }

}
