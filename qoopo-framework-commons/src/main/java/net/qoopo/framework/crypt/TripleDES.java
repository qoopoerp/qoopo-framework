/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.framework.crypt;

//import com.sun.jersey.core.util.Base64;
import java.io.Serializable;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Esta clase implementa los metodos para encriptacion y desencriptacion
 * utilizando el algoritmo TripleDES, esto para almacenar datos sensibles como
 * contrase;as de cuentas de correo
 *
 * @author Alberto Garcia
 * @version 1.0.0.1
 */
public class TripleDES implements Serializable{

    // Algoritmo utilizado para la encriptacion TripleDES
    private static final String ALGORITHM = "DESEde";
    // Valor para completar la clave a 3 bytes
    private static final byte PADDING_VALUE = 0x00;
    // Tamaño de la clave privada
    private static final byte KEY_LENGTH = 24;
    // Clave    
//    private static final String DEFAULT_STRING_KEY = "!@#@&$#$~!&*!dfasdgf18237";

    /**
     * Este metodo permite encriptar una clave o mensaje
     *
     * @param textoPlano dato a ser encriptado
     * @param password
     * @return Valor encriptado
     * @throws Exception
     */
    public static String cifrar(String textoPlano, String password) throws Exception {
        return new String(cifrar(textoPlano.getBytes("UTF8"), password.getBytes("UTF8")));
    }

    public static byte[] cifrar(byte[] plainBytes, String password) throws Exception {
        return cifrar(plainBytes, password.getBytes("UTF8"));
    }

    public static byte[] cifrar(byte[] plainBytes, byte[] encryptionKey) throws Exception {

        if (plainBytes == null) {
            return null;
        }
        if (encryptionKey == null) {
            throw new IllegalArgumentException("La valor no puede ser null");
        }

        byte[] cipherBytes = null;
        try {
            encryptionKey = paddingKey(encryptionKey, KEY_LENGTH, PADDING_VALUE);

            KeySpec keySpec = new DESedeKeySpec(encryptionKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            cipherBytes = cipher.doFinal(plainBytes);
            cipherBytes = Base64.encodeBase64(cipherBytes);
        } catch (Exception e) {
            throw new Exception("Error al cifrar " + e.getMessage());
        }

        return cipherBytes;
    }

    /**
     * Este metodo permite desencriptar una contraseña o un dato
     *
     * @param valorCifrado cadena encriptada
     * @param password
     * @return valor descifrado
     * @throws Exception si algo sale mal en le procesos
     */
    public static String descifrar(String valorCifrado, String password) throws Exception {
        return new String(descifrar(Base64.decodeBase64(valorCifrado.getBytes("UTF8")), password.getBytes("UTF8")), "UTF-8");
    }

    public static byte[] descifrar(byte[] plainBytes, String password) throws Exception {
        return descifrar(plainBytes, password.getBytes("UTF8"));
    }

    public static byte[] descifrar(byte[] bytesCifrado, byte[] encryptionKey) throws Exception {
        if (bytesCifrado == null) {
            return null;
        }
        if (encryptionKey == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }
        byte[] cipherBytes = null;
        try {
            encryptionKey = paddingKey(encryptionKey, KEY_LENGTH, PADDING_VALUE);
            KeySpec keySpec = new DESedeKeySpec(encryptionKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            cipherBytes = cipher.doFinal(bytesCifrado);
        } catch (Exception e) {
            throw new Exception("Error al desencriptar " + e.getMessage());
        }
        return cipherBytes;
    }

    private static byte[] paddingKey(byte[] b, int len, byte paddingValue) {
        byte[] newValue = new byte[len];
        if (b == null) {
            return newValue;
        }
        if (b.length >= len) {
            System.arraycopy(b, 0, newValue, 0, len);
            return newValue;
        }
        System.arraycopy(b, 0, newValue, 0, b.length);
        Arrays.fill(newValue, b.length, len, paddingValue);
        return newValue;
    }
}
