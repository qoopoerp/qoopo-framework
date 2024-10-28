
package net.qoopo.framework;

import java.math.BigDecimal;

/**
 * Utilitario que permit validar valores nulos
 * 
 * @author alberto
 */
public class NVL {

    public static String nvl(String valor) {
        return (valor != null) ? valor : "";// espacio
    }

    public static Object nvl(Object valor, Object alternativo) {
        return (valor != null) ? valor : alternativo;
    }

    public static BigDecimal nvl(BigDecimal valor) {
        return (valor != null) ? valor : BigDecimal.ZERO;
    }

    public static Number nvl(Number valor) {
        return (valor != null) ? valor : 0;
    }
}
