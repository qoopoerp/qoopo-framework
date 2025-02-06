package net.qoopo.framework.filter;

public class Util {

    public static String getTimeFormater(long diferencia) {

        StringBuilder sb = new StringBuilder();

        if (diferencia > (1000 * 60 * 60)) {
            sb.append(diferencia / (1000 * 60 * 60)).append("(hors) ");
            diferencia = diferencia % (1000 * 60 * 60);
        }

        if (diferencia > (1000 * 60)) {
            sb.append(diferencia / (1000 * 60)).append("(min) ");
            diferencia = diferencia % (1000 * 60);
        }

        if (diferencia > 1000) {
            sb.append(diferencia / 1000).append("(s) ");
            diferencia = diferencia % 1000;
        }

        sb.append(diferencia).append("(ms) ");
        return sb.toString();
    }

    public static String getTime(long inicial) {
        return "(" + getTimeFormater(System.currentTimeMillis() - inicial) + ")";
    }

    public static String nvl(String valor) {
        return (valor != null) ? valor : "";// espacio
    }
}
