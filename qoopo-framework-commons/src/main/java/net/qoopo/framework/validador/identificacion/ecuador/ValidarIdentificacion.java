
package net.qoopo.framework.validador.identificacion.ecuador;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
//import org.junit.Test;

/**
 * Validar cedula tomado de
 * https://github.com/maniac787/validacion-cedula-ruc-ecuador-java/blob/master/validacion-cedula-ruc-ecuador/src/ec/org/maniacSoft/validacion/ValidarIdentificacion.java
 *
 *
 *
 * 07/01/2020.- Actualización de acuerdo a artículo
 * https://www.jybaro.com/blog/cedula-de-identidad-ecuatoriana/ donde se
 * menciona que se confirma la existencia de cédulas con el tercer dígito 6, lo
 * cual era exclusivo para sociedades publicas. Se quita la validación del
 * tercer dígito
 *
 * @author alberto
 */
public class ValidarIdentificacion {

    // public static void main(String[] args) {
    // try {
    //
    // System.out.println("0963616339=" + validar("0963616339"));
    // System.out.println("0963616339001=" + validar("0963616339001"));
    // System.out.println("1768152560001=" + validar("1768152560001"));
    // System.out.println("9999999999999=" + validar("9999999999999"));
    //
    //// System.out.println("0963616339=" + validarCedula("0963616339"));// ruc raro
    // con tercer digito sin ser sociedad publica
    //// System.out.println("0963616339001=" +
    // validarRucPersonaNatural("0963616339001"));// ruc raro con tercer digito sin
    // ser sociedad publica
    //// System.out.println("0963616339001=" +
    // validarRucSociedadPrivada("0963616339001"));// ruc raro con tercer digito sin
    // ser sociedad publica
    //// System.out.println("0963616339001=" +
    // validarRucSociedadPublica("0963616339001"));// ruc raro con tercer digito sin
    // ser sociedad publica
    //// System.out.println("1768152560001=" +
    // validarRucSociedadPublica("1768152560001")); //CNT
    ////
    ////
    //// System.out.println("1717171717=" + validarCedula("1717171717"));
    //// System.out.println("0803033554=" + validarCedula("0803033554"));
    //// System.out.println("1717720542=" + validarCedula("1717720542"));
    //// System.out.println("1717720541=" + validarCedula("1717720541"));
    //// System.out.println("1717720541001=" +
    // validarRucPersonaNatural("1717720541001"));
    //// System.out.println("1717720541001=" +
    // validarRucSociedadPrivada("1717720541001"));
    //// System.out.println("0803033554001=" +
    // validarRucPersonaNatural("0803033554001"));
    //// System.out.println("0803033554001=" +
    // validarRucSociedadPrivada("0803033554001"));
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    public static RespuestaValidacion validar(String numero) {
        RespuestaValidacion r = new RespuestaValidacion(false, -1);
        try {
            if (numero.equalsIgnoreCase("9999999999999") || numero.equalsIgnoreCase("9999999999")) {
                r.setValido(true);
                r.setTipoIdentificacion(TipoDocumento.CONSUMIDOR_FINAL);
            } else if (validarCedula(numero)) {
                r.setValido(true);
                r.setTipoIdentificacion(TipoDocumento.CEDULA);
            } else if (validarRucPersonaNatural(numero)) {
                r.setValido(true);
                r.setTipoIdentificacion(TipoDocumento.RUC_NATURAL);
            } else if (validarRucSociedadPrivada(numero)) {
                r.setValido(true);
                r.setTipoIdentificacion(TipoDocumento.RUC_PRIVADA);
            } else if (validarRucSociedadPublica(numero)) {
                r.setValido(true);
                r.setTipoIdentificacion(TipoDocumento.RUC_PUBLICA);
            }
        } catch (Exception e) {

        }
        return r;
    }

    /**
     * @return true si es un documento v&aacutelido
     * @throws Exception
     */
    public static boolean validarCedula(String numero) throws Exception {
        try {
            validarInicial(numero, 10);
            validarCodigoProvincia(numero.substring(0, 2));
            validarTercerDigito(String.valueOf(numero.charAt(2)), TipoDocumento.CEDULA);
            algoritmoModulo10(numero, Integer.parseInt(String.valueOf(numero.charAt(9))));
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * @param numero de ruc persona natural
     * @return true si es un documento v&aacutelido
     * @throws Exception
     */
    public static boolean validarRucPersonaNatural(String numero) throws Exception {
        try {
            validarInicial(numero, 13);
            validarCodigoProvincia(numero.substring(0, 2));
            validarTercerDigito(String.valueOf(numero.charAt(2)), TipoDocumento.RUC_NATURAL);
            validarCodigoEstablecimiento(numero.substring(10, 13));
            algoritmoModulo10(numero.substring(0, 9), Integer.parseInt(String.valueOf(numero.charAt(9))));
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @param numero ruc empresa privada
     * @return
     * @throws Exception
     */
    public static boolean validarRucSociedadPrivada(String numero) throws Exception {
        // validaciones
        try {
            validarInicial(numero, 13);
            validarCodigoProvincia(numero.substring(0, 2));
            validarTercerDigito(String.valueOf(numero.charAt(2)), TipoDocumento.RUC_PRIVADA);
            validarCodigoEstablecimiento(numero.substring(10, 13));
            algoritmoModulo11(numero.substring(0, 9), Integer.parseInt(String.valueOf(numero.charAt(9))),
                    TipoDocumento.RUC_PRIVADA);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param numero ruc empresa public
     * @return
     * @throws Exception
     */
    public static boolean validarRucSociedadPublica(String numero) throws Exception {
        // validaciones
        try {
            validarInicial(numero, 13);
            validarCodigoProvincia(numero.substring(0, 2));
            validarTercerDigito(String.valueOf(numero.charAt(2)), TipoDocumento.RUC_PUBLICA);
            validarCodigoEstablecimiento(numero.substring(10, 13));
            algoritmoModulo11(numero.substring(0, 8), Integer.parseInt(String.valueOf(numero.charAt(8))),
                    TipoDocumento.RUC_PUBLICA);
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * @param numero
     * @param caracteres
     * @return
     * @throws Exception
     */
    protected static boolean validarInicial(String numero, int caracteres) throws Exception {
        if (StringUtils.isEmpty(numero)) {
            throw new Exception("Valor no puede estar vacio");
        }

        if (!NumberUtils.isDigits(numero)) {
            throw new Exception("Valor ingresado solo puede tener dígitos");
        }

        if (numero.length() != caracteres) {
            throw new Exception("Valor ingresado debe tener " + caracteres + " caracteres");
        }

        return true;
    }

    /**
     * @param n&uacutemero en el rango de n&uacutemeros de provincias del
     *                     ecuador
     * @return
     * @throws Exception
     */
    protected static boolean validarCodigoProvincia(String numero) throws Exception {
        if (Integer.parseInt(numero) < 0 || Integer.parseInt(numero) > 24) {
            throw new Exception("Codigo de Provincia (dos primeros dígitos) no deben ser mayor a 24 ni menores a 0");
        }

        return true;
    }

    /**
     * @param numero
     * @param tipo   de documento cedula, ruc
     * @return
     * @throws Exception
     */
    protected static boolean validarTercerDigito(String numero, Integer tipo) throws Exception {
        switch (tipo) {
            case TipoDocumento.CEDULA:
            case TipoDocumento.RUC_NATURAL:
                // if (Integer.parseInt(numero) < 0 || Integer.parseInt(numero) > 5) {
                if (Integer.parseInt(numero) < 0 || Integer.parseInt(numero) > 6) {
                    // throw new Exception("Tercer dígito debe ser mayor o igual a 0 y menor a 6
                    // para cédulas y RUC de persona natural ... permitidos de 0 a 5");
                    throw new Exception(
                            "Tercer dígito debe ser mayor o igual a 0 y menor a 7 para cédulas y RUC de persona natural ... permitidos de 0 a 5");
                }
                break;
            case TipoDocumento.RUC_PRIVADA:
                if (Integer.parseInt(numero) != 9) {
                    throw new Exception("Tercer dígito debe ser igual a 9 para sociedades privadas");
                }
                break;

            case TipoDocumento.RUC_PUBLICA:
                if (Integer.parseInt(numero) != 6) {
                    throw new Exception("Tercer dígito debe ser igual a 6 para sociedades públicas");
                }
                break;
            default:
                throw new Exception("Tipo de Identificacion no existe.");
        }

        return true;
    }

    /**
     * @param digitosIniciales
     * @param digitoVerificador
     * @return
     * @throws Exception
     */
    protected static boolean algoritmoModulo10(String digitosIniciales, int digitoVerificador) throws Exception {
        Integer[] arrayCoeficientes = new Integer[] { 2, 1, 2, 1, 2, 1, 2, 1, 2 };

        Integer[] digitosInicialesTMP = new Integer[digitosIniciales.length()];
        int indice = 0;
        for (char valorPosicion : digitosIniciales.toCharArray()) {
            digitosInicialesTMP[indice] = NumberUtils.createInteger(String.valueOf(valorPosicion));
            indice++;
        }

        int total = 0;
        int key = 0;
        for (Integer valorPosicion : digitosInicialesTMP) {
            if (key < arrayCoeficientes.length) {
                valorPosicion = (digitosInicialesTMP[key] * arrayCoeficientes[key]);

                if (valorPosicion >= 10) {
                    char[] valorPosicionSplit = String.valueOf(valorPosicion).toCharArray();
                    valorPosicion = (Integer.parseInt(String.valueOf(valorPosicionSplit[0])))
                            + (Integer.parseInt(String.valueOf(valorPosicionSplit[1])));

                }
                total = total + valorPosicion;
            }

            key++;
        }
        int residuo = total % 10;
        int resultado;

        if (residuo == 0) {
            resultado = 0;
        } else {
            resultado = 10 - residuo;
        }

        if (resultado != digitoVerificador) {
            throw new Exception("Dígitos iniciales no validan contra Dígito Idenficador");
        }

        return true;
    }

    /**
     * @param numero
     * @return
     * @throws Exception
     */
    protected static boolean validarCodigoEstablecimiento(String numero) throws Exception {
        if (Integer.parseInt(numero) < 1) {
            throw new Exception("Código de establecimiento no puede ser 0");
        }
        return true;
    }

    /**
     * @param digitosIniciales
     * @param digitoVerificador
     * @param tipo
     * @return
     * @throws Exception
     */
    protected static boolean algoritmoModulo11(String digitosIniciales, int digitoVerificador, Integer tipo)
            throws Exception {
        Integer[] arrayCoeficientes = null;

        // System.out.println("digitos iniciales:" + digitosIniciales);
        switch (tipo) {

            case TipoDocumento.RUC_PRIVADA:
                arrayCoeficientes = new Integer[] { 4, 3, 2, 7, 6, 5, 4, 3, 2 };
                break;
            case TipoDocumento.RUC_PUBLICA:
                arrayCoeficientes = new Integer[] { 3, 2, 7, 6, 5, 4, 3, 2 };
                break;
            default:
                throw new Exception("Tipo de Identificacion no existe.");
        }

        Integer[] digitosInicialesTMP = new Integer[digitosIniciales.length()];
        int indice = 0;
        for (char valorPosicion : digitosIniciales.toCharArray()) {
            digitosInicialesTMP[indice] = NumberUtils.createInteger(String.valueOf(valorPosicion));
            indice++;
        }

        int total = 0;
        int key = 0;
        for (Integer valorPosicion : digitosInicialesTMP) {
            if (key < arrayCoeficientes.length) {
                valorPosicion = (digitosInicialesTMP[key] * arrayCoeficientes[key]);

                // if (valorPosicion >= 10) {
                // char[] valorPosicionSplit = String.valueOf(valorPosicion).toCharArray();
                // valorPosicion = (Integer.parseInt(String.valueOf(valorPosicionSplit[0]))) +
                // (Integer.parseInt(String.valueOf(valorPosicionSplit[1])));
                //// System.out.println(valorPosicion);
                // }
                total = total + valorPosicion;
            }

            key++;
        }

        int residuo = total % 11;
        int resultado;

        if (residuo == 0) {
            resultado = 0;
        } else {
            resultado = (11 - residuo);
        }

        if (resultado != digitoVerificador) {
            throw new Exception("Dígitos iniciales no validan contra Dígito Idenficador [" + resultado + "/"
                    + digitoVerificador + "]");
        }

        return true;
    }
}
