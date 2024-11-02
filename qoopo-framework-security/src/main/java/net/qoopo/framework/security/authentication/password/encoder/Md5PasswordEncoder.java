package net.qoopo.framework.security.authentication.password.encoder;

import net.qoopo.framework.crypt.Hash;

public class Md5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        return Hash.MD5(password);
    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        return Hash.MD5(clearPassword).equalsIgnoreCase(encodedPassword);
    }

    // @Override
    // public String getPasswordEncoded() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getPasswordEncoded'");
    // }

}
