
package net.qoopo.framework.strings;

/**
 * Utilitario que permite enmascarar un valor como numeros de telefono o tarjetas de
 * credito
 *
 * @author alberto
 */
public class Enmascarar {

    public static int START_LEN = 3, END_LEN = 3;

    public static String enmascarar(String original) {

        try {
            // long starttime = System.currentTimeMillis();
            int total = original.length();

            int masklen = total - (START_LEN + END_LEN);
            StringBuilder maskedbuf = new StringBuilder(original.substring(0, START_LEN));
            for (int i = 0; i < masklen; i++) {
                maskedbuf.append('X');
            }
            maskedbuf.append(original.substring(START_LEN + masklen, total));
            String masked = maskedbuf.toString();
            return masked;
        } catch (Exception e) {
            return original;
        }
    }

    // public static void main(String[] args) {
    // List<String> cadenas = new ArrayList<>();
    // cadenas.add("0803033554");
    // cadenas.add("1234567897");
    // cadenas.add("ALBERTO ISAAC GARCIA PATIÑO");
    // cadenas.add("PEDRITO COCO");
    // cadenas.add("EL NOMBRE DE CUALQUIER PERSONA");
    // cadenas.add("UNITO");
    // cadenas.add("UNO");
    // cadenas.add("LIMPIAR");
    // cadenas.add("APUESTA A LO SEGURO");
    // for (String cadena : cadenas) {
    // System.out.println(cadena + ":" + enmascarar(cadena));
    // }
    // }
}
