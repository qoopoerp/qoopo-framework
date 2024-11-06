package net.qoopo.qoopo.security.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import net.qoopo.framework.security.authentication.password.encoder.Argon2PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.BCryptPasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.DefaulPasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.Md5PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.Sha256PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.Sha512PasswordEncoder;
import net.qoopo.framework.security.authentication.password.encoder.ShaPasswordEncoder;

public class PasswordEncodersTest {

    private static Logger log = Logger.getLogger("passwords-encoders-test");

    @Test
    public void passwordsEncoders() {
        try {
            String password = "superpassword";
            String encodedPassword;

            log.info("password=" + password);
            PasswordEncoder passwordEncoder = new DefaulPasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("default -> [" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));

            passwordEncoder = new Md5PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("md5 -> [" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue("d1e576b71ccef5978d221fadf4f0e289".equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new ShaPasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("SHA -> [" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue("77be4fc97f77f5f48308942bb6e32aacabed9cef".equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new Sha256PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("sha256 -> [" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue("ba21767ae494afe5a2165dcb3338c5323e9907050e34542c405d575cc31bf527"
                    .equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new Sha512PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("sha512 -> [" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));
            assertTrue(
                    "111fca2d52def4c33f4d8f1be7e74d14b65d365e5ddb91610c3c0dbecc192073b0b0df28213e3828cc0321f6286baf94449a4f8803203be3293595f4d67ff7e2"
                            .equalsIgnoreCase(passwordEncoder.encode(password)));

            passwordEncoder = new BCryptPasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("Bcrypt -> [" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));

            passwordEncoder = new Argon2PasswordEncoder();
            encodedPassword = passwordEncoder.encode(password);
            log.info("Argon2 -> [" + encodedPassword + "]");
            assertTrue(passwordEncoder.validate(password, encodedPassword));

            passwordEncoder = new BCryptPasswordEncoder();
            encodedPassword = passwordEncoder.encode("recoveredpassword");
            log.info("recoveredpassword -> [" + encodedPassword + "]");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
