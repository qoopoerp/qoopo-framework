package net.qoopo.qoopoframework.jpa.core.interfaces;

import java.util.List;

import net.qoopo.qoopoframework.models.Columna;
import net.qoopo.qoopoframework.models.OpcionBase;

/**
 * Esta interfaz es para las entidades que deben ser agrupables.
 *
 * @author alberto
 */
public interface Agrupable {

    /**
     * Devuelve las opciones de grupos
     *
     * @return
     */
    public List<OpcionBase> getOpcionesGrupos();

    /**
     * Devuelve las posibles columnas a mostrar
     *
     * @return
     */
    public List<Columna> getColumnas();

    /**
     * Obtiene el grupo, grupos o clave para mostrar el grafico. Puede ser
     * varios valores en caso que se agrupo por un item del detalle, como el
     * producto desde la orden de venta
     *
     * @param opcion
     * @return
     */
    public List<String> getGrupo(OpcionBase opcion);

}
