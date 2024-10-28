package net.qoopo.framework.strings;

/**
 * Utilitario que permite limpiar caracteres especiales de una cadena
 * 
 * @author alberto
 */
public class EliminarCaracteres {

    /**
     * Limpia caracteres especiales de una cadena
     * 
     * @param cadena
     * @return
     */
    public static String limpiar(String cadena) {
        // primero reemplaza los caracteres acentados
        cadena = reemplaza(cadena);
        // luego elimina caracteres que no esten en el match
        cadena = cadena.replaceAll("[^\\dA-Za-z .]", "");
        return cadena;
    }

    /**
     * Función que elimina acentos y caracteres especiales de una cadena de
     * texto.
     *
     * @param input
     * @return cadena de texto limpia de acentos y caracteres especiales.
     */
    public static String reemplaza(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        } // for i
        return output;
    }// remove1

    // /**
    // * Función que elimina acentos y caracteres especiales de una cadena de
    // * texto.
    // *
    // * @param input
    // * @return cadena de texto limpia de acentos y caracteres especiales.
    // */
    // public static String remove2(String input) {
    // // Descomposición canónica
    // String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    // // Nos quedamos únicamente con los caracteres ASCII
    // Pattern pattern = Pattern.compile("\\p{ASCII}+");
    //// Pattern pattern = Pattern.compile("\\p{ASCII}");
    // return pattern.matcher(normalized).replaceAll("");
    // }//remove2

    /*
     * public static void main(String[] args) {
     * // String valor = "‘HDXU-----?s-";
     * String valor =
     * "‘hola ñaño, el clima está mejor que nunca. El paraíso hindú amplía mis expectativas"
     * ;
     * System.out.println("original=" + valor);
     * System.out.println(reemplaza(valor));
     * // System.out.println(remove2(valor));
     * System.out.println(limpiar(valor));
     * }
     */
}
