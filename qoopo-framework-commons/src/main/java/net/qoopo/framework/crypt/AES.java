package net.qoopo.framework.crypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    //"!@#@&$#$~!&*!dfasdgf18237"
    public static Crypter CRYPTER128;// = new Crypter(AES.getCipher128("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, true));
    public static Crypter DECRYPTER128;// = new Crypter(AES.getCipher128("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, false));

    public static Crypter CRYPTER256;// = new Crypter(AES.getCipher256("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, true));
    public static Crypter DECRYPTER256;// = new Crypter(AES.getCipher256("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, false));

    public static final String ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    public static final String ALGORITHM_ECB = "AES/ECB/PKCS5Padding";

    static {
        try {
            CRYPTER128 = new Crypter(AES.getCipher128("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, true));
            DECRYPTER128 = new Crypter(AES.getCipher128("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, false));

            CRYPTER256 = new Crypter(AES.getCipher256("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, true));
            DECRYPTER256 = new Crypter(AES.getCipher256("!@#@&$#$~!&*!dfasdgf18237", null, AES.ALGORITHM_ECB, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Cipher getCipher128(String key, String iv, String algorithm, boolean encrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
//        try {
        IvParameterSpec ivParam = null;
        if (iv != null) {
            if (iv.length() < 16) {
                iv = padRight(iv, '0', 16);
            } else if (iv.length() > 16) {
                iv = iv.substring(0, 15);
            }
            ivParam = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        }
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = md.digest();
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, 0, 16, "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        if (encrypt) {
            if (ivParam == null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
            }
        } else {
            if (ivParam == null) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
            }
        }
        return cipher;
//        } catch (Exception ex) {
//
//        }
//        return null;
    }

    public static Cipher getCipher256(String key, String iv, String algorithm, boolean encrypt) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        if (key == null) {
            return null;
        }
        IvParameterSpec ivParam = null;
        if (iv != null) {
            if (iv.length() < 16) {
                iv = padRight(iv, '0', 16);
            } else if (iv.length() > 16) {
                iv = iv.substring(0, 15);
            }
            ivParam = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        }
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = md.digest();
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        if (encrypt) {
            if (ivParam == null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
            }
        } else {
            if (ivParam == null) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
            }
        }
        return cipher;
    }

    public static Cipher getCipher256(byte[] key, String iv, String algorithm, boolean encrypt) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (key == null) {
            return null;
        }
        IvParameterSpec ivParam = null;
        if (iv != null) {
            if (iv.length() < 16) {
                iv = padRight(iv, '0', 16);
            } else if (iv.length() > 16) {
                iv = iv.substring(0, 15);
            }
            ivParam = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        }
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key);
        byte[] keyBytes = md.digest();
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        if (encrypt) {
            if (ivParam == null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
            }
        } else {
            if (ivParam == null) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
            }
        }
        return cipher;
    }

    private static String padLeft(String inputString, char letter, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(letter);
        }
        sb.append(inputString);

        return sb.toString();
    }

    private static String padRight(String inputString, char letter, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(inputString);
        while (sb.length() < length) {
            sb.append(letter);
        }

        return sb.toString();
    }

}
