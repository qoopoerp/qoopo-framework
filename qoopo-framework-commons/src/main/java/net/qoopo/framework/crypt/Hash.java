/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.framework.crypt;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Esta clase permite la ejecucion de algoritmos de cifrados y hashes para
 * contraseñas
 *
 * @author Alberto Garcia
 * @version 1.0.0.1
 */
public class Hash implements Serializable {

//    private static final String DEFAULT_STRING_KEY = "alskhADeak183hD37AjhF4761BfjAdp3";
    /**
     * Contraseña con la cual se cifra los textos dentro de Qoopo
     */
    public static final String DEFAULT_STRING_KEY = "!@#@&$#$~!&*!dfasdgf18237";

    /**
     * Crea un hash MD5
     *
     * @param cadena
     * @return
     */
    public static String MD5(String cadena) {
        String resultado = "";
        try {
            MessageDigest dm = MessageDigest.getInstance("MD5");
            try {
                dm.update(cadena.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                dm.update(cadena.getBytes());
            }
            byte messageDigest[] = dm.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            resultado = hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(General.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado.toUpperCase();
    }

    /**
     * Crea un hast SHA
     *
     * @param cadena
     * @return
     */
    public static String SHA(String cadena) {
        String resultado = "";
        try {
            MessageDigest dm = MessageDigest.getInstance("SHA");
            dm.update(cadena.getBytes());
            byte messageDigest[] = dm.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            resultado = hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(General.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado.toUpperCase();
    }

    /**
     * Crea una hast SHA256
     *
     * @param cadena
     * @return
     */
    public static String SHA256(String cadena) {
        String resultado = "";
        try {
            MessageDigest dm = MessageDigest.getInstance("SHA-256");
            dm.update(cadena.getBytes());
            byte messageDigest[] = dm.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            resultado = hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(General.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado.toUpperCase();
    }

    /**
     * Crea un hast 256
     *
     * @param cadena
     * @return
     */
    public String SHA512(String cadena) {
        String resultado = "";
        try {
            MessageDigest dm = MessageDigest.getInstance("SHA-512");
            dm.update(cadena.getBytes());
            byte messageDigest[] = dm.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            resultado = hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(General.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado.toUpperCase();
    }

    /**
     * Crea un hash de la password del sistema
     *
     * @param cadena
     * @return
     */
    public static String HASH(String cadena) {
        if (cadena == null) {
            return null;
        }
        return SHA256(SHA(SHA256(cadena))).toUpperCase();
    }

}
