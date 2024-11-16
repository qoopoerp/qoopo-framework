package net.qoopo.framework;

import java.io.Serializable;

/**
 *
 * @author alberto
 */
public abstract class Accion implements Serializable {

    public abstract Object ejecutar(Object... JpaParameterss);

    /**
     * Verifica si la accion es difeente de nula y ejecuta la accion.No se
     * realiza control de errores, se debe realizar dentro de ejecutar de cada
     * accion
     *
     * @param accion
     * @param JpaParameterss
     */
    public static void verificaEjecuta(Accion accion, Object... JpaParameterss) {
        if (accion != null) {
            accion.ejecutar(JpaParameterss);
        }
    }
}
