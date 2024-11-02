package net.qoopo.framework.security.authentication.password.encoder;

import org.mindrot.jbcrypt.BCrypt;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

// import org.bouncycastle.crypto.generators.BCrypt;

public class Argon2PasswordEncoder implements PasswordEncoder {

    // Parámetros de configuración
    private int saltLength = 16; // Tamaño del salt en bytes
    private int hashLength = 32; // Tamaño del hash en bytes
    private int iterations = 2; // Número de iteraciones
    private int memory = 65536; // Uso de memoria en KB (64 MB)
    private int parallelism = 1; // Número de hilos

    private Argon2 argon2;

    public Argon2PasswordEncoder() {
        argon2 = Argon2Factory.create();
    }

    public Argon2PasswordEncoder(int saltLength, int hashLength, int iterations, int memory, int parallelism) {
        this();
        this.saltLength = saltLength;
        this.hashLength = hashLength;
        this.iterations = iterations;
        this.memory = memory;
        this.parallelism = parallelism;
    }

    @Override
    public String encode(String password) {
        return argon2.hash(iterations, memory, parallelism, password.toCharArray());

    }

    @Override
    public boolean validate(String clearPassword, String encodedPassword) {
        return argon2.verify(encodedPassword, clearPassword.toCharArray());
    }

    // @Override
    // public String getPasswordEncoded() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getPasswordEncoded'");
    // }

}
