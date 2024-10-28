package net.qoopo.framework.number;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class NumeroFormato {

    private static final Locale LOCALIZACION_ECUADOR = new Locale("es", "EC");
    private static NumberFormat nf = NumberFormat.getInstance(LOCALIZACION_ECUADOR);

    /**
     * Número de digitos por defecto utilizados en el sistema
     */
    private static final int DIGITOS_DECIMALES = 2;

    private static void formato(int digitosDecimales) {
        int dd = digitosDecimales;
        nf.setMaximumFractionDigits(dd);
        nf.setMinimumFractionDigits(dd);
        nf.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static double formatoDouble(double numero) {
        return convertirDoubleToBigDecimal(numero).doubleValue();
    }

    public static double formatoDouble(final int digitosDecimales,
            final double numero) {
        return convertirDoubleToBigDecimal(digitosDecimales, numero)
                .doubleValue();
    }

    public static BigDecimal formatoDecimal(final int digitosDecimales,
            final double numero) {
        String valor = String.valueOf(numero);
        BigDecimal bd = new BigDecimal(valor);
        return bd.setScale(digitosDecimales, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertirDoubleToBigDecimal(final double numero) {
        Double valor = numero;
        BigDecimal bd = new BigDecimal(valor.toString());
        return bd.setScale(DIGITOS_DECIMALES, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertirDoubleToBigDecimal(
            final int digitosDecimales, final double numero) {
        Double valor = numero;
        BigDecimal bd = new BigDecimal(valor.toString());
        return bd.setScale(digitosDecimales, RoundingMode.HALF_UP);
    }

    public static String formatoDecimal(final int digitosDecimales, BigDecimal numero) {
        formato(digitosDecimales);
        if (numero != null) {
            return nf.format(numero);
        } else {
            return nf.format(BigDecimal.ZERO);
        }
    }

}
