package net.qoopo.framework.strings;

/**
 * Clase tomada del internet para convertir numeros a letras Origen:
 * http://axiacore.com/2008/09/convertir-numeros-a-letras-en-java/ Movido a:
 * https://github.com/Axiacore/number-to-letter-java
 *
 *
 * @author ALBERTO
 *
 */
public abstract class ConvertirNumerosLetras {

    /**
     * Esta clase provee la funcionalidad de convertir un numero representado en
     * digitos a una representacion en letras. Mejorado para leer centavos
     *
     * @author Camilo Nova
     * @version 1.0
     */
    // public abstract class NumberToLetterConverter {
    private static final String[] UNIDADES = { "", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ",
            "OCHO ", "NUEVE ", "DIEZ ",
            "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE",
            "VEINTE" };

    private static final String[] DECENAS = { "VEINTI", "TREINTA ", "CUARENTA ", "CINCUENTA ", "SESENTA ", "SETENTA ",
            "OCHENTA ", "NOVENTA ", "CIEN " };

    private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ",
            "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };

    public static String convertirNumeroALetra(Double number) {
        return convertirNumeroALetra(String.valueOf(number));
    }

    /**
     * Convierte un numero en representacion numerica a uno en representacion de
     * texto. El numero es valido si esta entre 0 y 999'999.999
     * <p>
     * Creation date 3/05/2006 - 05:37:47 PM
     *
     * @param number Numero a convertir
     * @return Numero convertido a texto
     * @throws NumberFormatException Si el numero esta fuera del rango
     * @since 1.0
     */
    public static String convertirNumeroALetra(String number) throws NumberFormatException {
        String converted = new String();

        // Validamos que sea un numero legal
        // double doubleNumber = Math.round(number);
        // Cambio realizado por alberto garcia para que tome los decimales
        double doubleNumber = Double.parseDouble(number);

        if (doubleNumber > 999999999) {
            throw new NumberFormatException("El numero es mayor de 999'999.999, " + "no es posible convertirlo");
        }

        String splitNumber[] = String.valueOf(number).replace('.', '#').split("#");

        // Descompone el trio de millones - ¡SGT!
        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 8))
                + String.valueOf(getDigitAt(splitNumber[0], 7))
                + String.valueOf(getDigitAt(splitNumber[0], 6)));
        if (millon == 1) {
            converted = "UN MILLON ";
        }
        if (millon > 1) {
            converted = convertNumber(String.valueOf(millon)) + "MILLONES ";
        }

        // Descompone el trio de miles - ¡SGT!
        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 5))
                + String.valueOf(getDigitAt(splitNumber[0], 4))
                + String.valueOf(getDigitAt(splitNumber[0], 3)));
        if (miles == 1) {
            converted += "MIL ";
        }
        if (miles > 1) {
            converted += convertNumber(String.valueOf(miles)) + "MIL ";
        }

        // Descompone el ultimo trio de unidades - ¡SGT!
        int cientos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 2))
                + String.valueOf(getDigitAt(splitNumber[0], 1))
                + String.valueOf(getDigitAt(splitNumber[0], 0)));
        if (cientos == 1) {
            converted += "UN";
        }

        if (millon + miles + cientos == 0) {
            converted += "CERO";
        }
        if (cientos > 1) {
            converted += convertNumber(String.valueOf(cientos));
        }

        converted += " DOLARES";

        // Descompone los centavos - Camilo
        int centavos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[1], 2))
                + String.valueOf(getDigitAt(splitNumber[1], 1))
                + String.valueOf(getDigitAt(splitNumber[1], 0)));
        if (centavos == 1) {
            converted += " CON UN CENTAVO";
        }
        if (centavos > 1) {
            converted += " CON " + convertNumber(String.valueOf(centavos)) + " CENTAVOS";
        }

        return converted;
    }

    /**
     * Convierte un numero a letras, los centavos se representan en el formato
     * ##/100
     *
     * @param number
     * @return
     * @throws NumberFormatException
     */
    public static String convertirNumeroALetraCentavosNumero(String number) throws NumberFormatException {
        String converted = new String();

        // Validamos que sea un numero legal
        // double doubleNumber = Math.round(number);
        // Cambio realizado por alberto garcia para que tome los decimales
        double doubleNumber = Double.parseDouble(number);

        if (doubleNumber > 999999999) {
            throw new NumberFormatException("El numero es mayor de 999'999.999, " + "no es posible convertirlo");
        }

        String splitNumber[] = String.valueOf(number).replace('.', '#').split("#");

        // Descompone el trio de millones - ¡SGT!
        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 8))
                + String.valueOf(getDigitAt(splitNumber[0], 7))
                + String.valueOf(getDigitAt(splitNumber[0], 6)));
        if (millon == 1) {
            converted = "UN MILLON ";
        }
        if (millon > 1) {
            converted = convertNumber(String.valueOf(millon)) + "MILLONES ";
        }

        // Descompone el trio de miles - ¡SGT!
        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 5))
                + String.valueOf(getDigitAt(splitNumber[0], 4))
                + String.valueOf(getDigitAt(splitNumber[0], 3)));
        if (miles == 1) {
            converted += "MIL ";
        }
        if (miles > 1) {
            converted += convertNumber(String.valueOf(miles)) + "MIL ";
        }

        // Descompone el ultimo trio de unidades - ¡SGT!
        int cientos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 2))
                + String.valueOf(getDigitAt(splitNumber[0], 1))
                + String.valueOf(getDigitAt(splitNumber[0], 0)));
        if (cientos == 1) {
            converted += "UN";
        }

        if (millon + miles + cientos == 0) {
            converted += "CERO";
        }
        if (cientos > 1) {
            converted += convertNumber(String.valueOf(cientos));
        }

        converted += " DOLARES";

        // Descompone los centavos - Camilo
        if (splitNumber.length > 1) {
            int centavos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[1], 2))
                    + String.valueOf(getDigitAt(splitNumber[1], 1))
                    + String.valueOf(getDigitAt(splitNumber[1], 0)));
            if (centavos == 1) {
                converted += " CON 01/100";
            }
            if (centavos > 1) {

                String p = "";
                if (centavos < 10) {
                    p = "0";
                }
                converted += " CON " + p + String.valueOf(centavos) + "/100";
            }
        }

        return converted;
    }

    /**
     * Convierte los trios de numeros que componen las unidades, las decenas y
     * las centenas del numero.
     * <p>
     * Creation date 3/05/2006 - 05:33:40 PM
     *
     * @param number Numero a convetir en digitos
     * @return Numero convertido en letras
     * @since 1.0
     */
    private static String convertNumber(String number) {
        if (number.length() > 3) {
            throw new NumberFormatException(
                    "La longitud maxima debe ser 3 digitos");
        }

        String output = new String();
        if (getDigitAt(number, 2) != 0) {
            output = CENTENAS[getDigitAt(number, 2) - 1];
        }

        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
                + String.valueOf(getDigitAt(number, 0)));

        if (k <= 20) {
            output += UNIDADES[k];
        } else {
            if (k > 30 && getDigitAt(number, 0) != 0) {
                output += DECENAS[getDigitAt(number, 1) - 2] + "Y "
                        + UNIDADES[getDigitAt(number, 0)];
            } else {
                output += DECENAS[getDigitAt(number, 1) - 2]
                        + UNIDADES[getDigitAt(number, 0)];
            }
        }

        // Caso especial con el 100
        if (getDigitAt(number, 2) == 1 && k == 0) {
            output = "CIEN ";
        }

        return output;
    }

    /**
     * Retorna el digito numerico en la posicion indicada de text-right a
     * text-left
     * <p>
     * Creation date 3/05/2006 - 05:26:03 PM
     *
     * @param origin   Cadena en la cual se busca el digito
     * @param position Posicion de text-right a text-left a retornar
     * @return Digito ubicado en la posicion indicada
     * @since 1.0
     */
    private static int getDigitAt(String origin, int position) {
        if (origin.length() > position && position >= 0) {
            return origin.charAt(origin.length() - position - 1) - 48;
        }
        return 0;
    }
    /*
     * public static void main(String[] args) {
     * try {
     * BigDecimal value=BigDecimal.valueOf(12638.050000589849);
     * String valor = value.toPlainString();
     * System.out.println("Valor=" + valor);
     * System.out.println("Valor letras=" +
     * convertirNumeroALetraCentavosNumero(valor));
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     */
}
