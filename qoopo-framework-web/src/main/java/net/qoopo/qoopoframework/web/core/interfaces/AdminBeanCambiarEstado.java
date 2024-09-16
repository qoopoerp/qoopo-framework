package net.qoopo.qoopoframework.web.core.interfaces;

import net.qoopo.qoopoframework.jpa.core.dtos.Estado;

/**
 * Interface que debe implementar los beans que manejan entidades que cambian de
 * estado
 *
 * @author alberto
 */
public interface AdminBeanCambiarEstado {

    /**
     * Metodo que procesara el cambio de estado.No debe ser llamado desde la
     * vista pues puede producir excepciones
     *
     * @param nuevoEstado
     * @throws java.lang.Exception
     */
    public void procesarEstado(int nuevoEstado) throws Exception;

    /**
     * Metodo que procesara el cambio de estado.No debe ser llamado desde la
     * vista pues puede producir excepciones
     *
     * @param estado
     * @throws java.lang.Exception
     */
    public void procesarEstado(Estado estado) throws Exception;

    /**
     * Metodo que sera llamado desde la vista. Su unica funciona es llamar al
     * metodo procesar estado y controlar las excepciones.
     *
     * @param nuevoEstado
     */
    public void cambiarEstado(int nuevoEstado);

}
