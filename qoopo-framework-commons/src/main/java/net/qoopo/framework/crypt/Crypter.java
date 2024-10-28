package net.qoopo.framework.crypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;

import net.qoopo.framework.file.FileUtil;

/**
 *
 * @author noroot
 */
public class Crypter {

    public static Crypter INSTANCE = null;

    private final int BUFFER_SIZE = 8192;

    private Cipher cipher = null;

    public Crypter(Cipher cipher) {
        this.cipher = cipher;
    }

    public Cipher getCipher() {
        return cipher;
    }

    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }

    public byte[] process(String plainText) {
        try {
            return process(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {

        }
        try {
            return plainText.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return plainText.getBytes();
        }
    }

    public byte[] process(byte[] input) throws IOException, IllegalBlockSizeException, BadPaddingException, Exception {
        if (cipher == null) {
            throw new NullPointerException();
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            byte[] tmp = cipher.update(input, 0, input.length);
            out.write(tmp);
            tmp = cipher.doFinal();
            out.write(tmp);
            return out.toByteArray();
        }
    }

    public byte[] process(InputStream input) throws IOException, BadPaddingException, Exception {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();) {
            byte[] buf = new byte[BUFFER_SIZE];
            int i;
            while ((i = input.read(buf)) > -1) {
                b.write(buf, 0, i);
            }
            return process(b.toByteArray());
        }
    }

    /**
     * funciona para cifrar o descifrar, pero carga todo el archivo de origen en
     * memoria
     *
     * @param src
     * @param dst
     * @throws FileNotFoundException
     * @throws BadPaddingException
     * @throws Exception
     */
    public void process(File src, File dst) throws FileNotFoundException, BadPaddingException, Exception {
        try (ByteArrayInputStream input = new ByteArrayInputStream(process(new FileInputStream(src))); FileOutputStream b = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > -1) {
                b.write(buf, 0, bytesRead);
            }
        }
        FileUtil.copyAttrs(src, dst);
    }

    /**
     * Procesa un cifrado, donde usa un CipherOutputStream para el destino
     *
     * @param src
     * @param dst
     * @throws FileNotFoundException
     * @throws BadPaddingException
     * @throws Exception
     */
    public void processCrypt(File src, File dst) throws FileNotFoundException, BadPaddingException, Exception {
        if (cipher == null) {
            throw new NullPointerException("Cipher is null");
        }
        try (InputStream inputStream = new FileInputStream(src); OutputStream outputStream = new FileOutputStream(dst); CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            // Encrypt input file and write in to output
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        }
        FileUtil.copyAttrs(src, dst);
    }

    /**
     * Procesa un cifrado, donde usa un CipherOutputStream para el destino
     *
     * @param inputStream
     * @param outputStream
     * @throws FileNotFoundException
     * @throws BadPaddingException
     * @throws Exception
     */
    public void processCrypt(InputStream inputStream, OutputStream outputStream) throws FileNotFoundException, BadPaddingException, Exception {
        if (cipher == null) {
            throw new NullPointerException("Cipher is null");
        }
        try (CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            // Encrypt input file and write in to output
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Procesa un descifrado, donde usa in CipherInputStream para el origen
     *
     * @param src
     * @param dst
     * @throws FileNotFoundException
     * @throws BadPaddingException
     * @throws Exception
     */
    public void processDecrypt(File src, File dst) throws FileNotFoundException, BadPaddingException, Exception {
        if (cipher == null) {
            throw new NullPointerException("Cipher is null");
        }
        try (InputStream is = new FileInputStream(src); OutputStream out = new FileOutputStream(dst); CipherInputStream cipherInputStream = new CipherInputStream(is, cipher)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        }
        FileUtil.copyAttrs(src, dst);
    }

    /**
     * Procesa un descifrado, donde usa in CipherInputStream para el origen
     *
     * @param is
     * @param out
     * @throws FileNotFoundException
     * @throws BadPaddingException
     * @throws Exception
     */
    public void processDecrypt(InputStream is, OutputStream out) throws FileNotFoundException, BadPaddingException, Exception {
        if (cipher == null) {
            throw new NullPointerException("Cipher is null");
        }
        try (CipherInputStream cipherInputStream = new CipherInputStream(is, cipher)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

}
