package net.qoopo.qoopoframework.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Logger para el sistema
 *
 * @author alberto
 */
public class QLogger {

    private static final Logger GENERAL = Logger.getLogger("Qoopo");

    private static void removerHandlers(Logger logger) {
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
    }

    private QLogger() {
        // Loguer
    }

    /**
     * Esta funcion nos permite convertir el stackTrace en un String, necesario
     * para poder imprimirlos al log debido a cambios en como Java los maneja
     * internamente
     *
     * @param e Excepcion de la que queremos el StackTrace
     * @return StackTrace de la excepcion en forma de String
     */
    public static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);
        return sWriter.toString();
    }

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

    public static void testError() {
        try {
            BigDecimal test = new BigDecimal("0.00");
            BigDecimal nuevo = new BigDecimal("50");
            nuevo = nuevo.divide(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
