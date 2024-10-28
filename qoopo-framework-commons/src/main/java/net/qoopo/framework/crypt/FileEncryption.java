/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.qoopo.framework.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * cifrado equitativo a cifrar con openssl
 *
 * cifrar:openssl enc -aes-256-ecb -p -nosalt -in $file -k "password" -out
 * $file.enc
 *
 * descifrar: openssl aes-256-ecb -d -nosalt -p -in file.enc -k password -out
 * file
 *
 * @author noroot
 */
public class FileEncryption {

    private static final int BUFFER_SIZE = 1024; //1024/2048

//    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String TRANSFORMATION = "AES";

    private final Cipher ecipher;
    private final Cipher dcipher;
    private SecretKey secretKey;

    /**
     * Initialize the ciphers using the given key.
     *
     * @param key
     * @param keyBits
     * @param useSalt
     */
    public FileEncryption(String key, int keyBits, boolean useSalt) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(key.getBytes("UTF-8"));
            byte[] keyBytes = sha256.digest();
            secretKey = new SecretKeySpec(keyBytes, TRANSFORMATION);
            ecipher = Cipher.getInstance(ALGORITHM);
            dcipher = Cipher.getInstance(ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize encryption.", e);
        }

    }

    public void encryptFile(File src, File dest) {
        try ( InputStream inputStream = new FileInputStream(src);  OutputStream outputStream = new FileOutputStream(dest);  CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, ecipher)) {
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            // Encrypt input file and write in to output
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println("error encryption" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void decryptFile(File srcFile, File destFile) {
        try ( InputStream is = new FileInputStream(srcFile);  OutputStream out = new FileOutputStream(destFile);  CipherInputStream cis = new CipherInputStream(is, dcipher)) {
            dcipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println("error decr" + e.getMessage());
            e.printStackTrace();
        }
    }
}
