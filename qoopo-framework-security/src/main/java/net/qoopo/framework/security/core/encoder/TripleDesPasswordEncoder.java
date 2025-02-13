package net.qoopo.framework.security.core.encoder;

import net.qoopo.framework.crypt.TripleDES;

public class TripleDesPasswordEncoder implements PasswordEncoder {

    private String passwordEncoder;

    public TripleDesPasswordEncoder(String passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String password) {
        try {
            return TripleDES.cifrar(password, passwordEncoder);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        return encode(clearPassword).equalsIgnoreCase(encodedPassword);
    }

}
