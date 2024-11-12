package net.qoopo.framework.security.core.encoder;

import org.mindrot.jbcrypt.BCrypt;

// import org.bouncycastle.crypto.generators.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {

    private static final int DEFAULT_ROUNDS = 12;
    private int rounds = 0;

    public BCryptPasswordEncoder() {
        rounds = DEFAULT_ROUNDS;
    }

    public BCryptPasswordEncoder(int rounds) {
        this.rounds = rounds;
    }

    @Override
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(rounds));

    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        try {
            return BCrypt.checkpw(clearPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    // @Override
    // public String getPasswordEncoded() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getPasswordEncoded'");
    // }

}
