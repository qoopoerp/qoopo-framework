package net.qoopo.qoopoframework.util;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

/**
 * Utilidades varias
 *
 * @author alberto
 */
public class QoopoUtil implements Serializable {

    private static final BigDecimal ZERO_COMPA = BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);

    // public static QoopoFormater getFormateador() {
    // return ((QoopoFormater) FacesUtils.getManagedBean("qoopoFormater"));
    // }
    public static String nvl(String valor) {
        return (valor != null) ? valor : "";// espacio
    }

    public static Object nvl(Object valor, Object alternativo) {
        return (valor != null) ? valor : alternativo;
    }

    public static String recortar(String cadena, int limite) {
        return cadena.length() > limite ? cadena.substring(0, limite) : cadena;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDate(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            getSDF().parse(strNum);
        } catch (Exception nfe) {
            try {
                getSDFMM().parse(strNum);
            } catch (Exception nfe1) {
                try {
                    getSDFYYYY().parse(strNum);
                } catch (Exception nfe2) {
                    try {
                        getSDFYYYYMM().parse(strNum);
                    } catch (Exception nfe3) {
                        try {
                            getSDFminimo().parse(strNum);
                        } catch (Exception nfe4) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Devuelve nulo si el valor es cero
     *
     * @param valor
     * @return
     */
    public static BigDecimal noZero(BigDecimal valor) {
        // return ((BigDecimal) nvl(valor, BigDecimal.ZERO)).compareTo(BigDecimal.ZERO)
        // == 0 ? null : valor;
        return ((BigDecimal) nvl(valor, BigDecimal.ZERO)).setScale(4, RoundingMode.HALF_UP).compareTo(ZERO_COMPA) == 0
                ? null
                : valor;
    }

    public static String print(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Metodo usado para lanzar una excepcion
     */
    public static void launchError() {
        int value = 5;
        value -= 5;
        value = 150 / value;
    }

    public static void launchErrorTrace() {
        try {
            int value = 5;
            value -= 5;
            value = 150 / value;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DateTimeFormatter getSDF() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    public static DateTimeFormatter getSDFminimo() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public static DateTimeFormatter getSDFYYYYMM() {
        return DateTimeFormatter.ofPattern("yyyy/MM");
    }

    public static DateTimeFormatter getSDFMM() {
        return DateTimeFormatter.ofPattern("MM");
    }

    public static DateTimeFormatter getSDFYYYY() {
        return DateTimeFormatter.ofPattern("yyyy");
    }

}
