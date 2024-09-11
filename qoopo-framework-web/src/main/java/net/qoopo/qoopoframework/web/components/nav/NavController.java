package net.qoopo.qoopoframework.web.components.nav;

import java.io.Serializable;

import net.qoopo.util.Accion;

/**
 * Controller del componente qoopo:nav. Este componenete permite realizar la
 * nevegacion en la vista formulario entre, primero, anterior, siguiente, ultimo
 *
 * @author alberto
 */
public class NavController implements Serializable {

    private int total;
    private int actual;
    private final Accion accionSeleccionar;
    private final Accion accionGetTotal;

    public NavController(Accion accionSeleccionar, Accion accionGetTotal) {
        this.accionSeleccionar = accionSeleccionar;
        this.accionGetTotal = accionGetTotal;
    }

    private void cleanActual() {
        if (actual > getTotal()) {
            actual = getTotal();
        }
        if (actual < 1) {
            actual = 1;
        }
    }

    /**
     * Recorre al objeto anterior de la lista
     */
    public void anterior() {
        if (actual > 1) {
            actual--;
        }
        cleanActual();
        accionSeleccionar.ejecutar(actual);
    }

    /**
     * Recorre al siguiente objeto de la lista
     */
    public void siguiente() {
        if (actual < getTotal()) {
            actual++;
        }
        cleanActual();
        accionSeleccionar.ejecutar(actual);
    }

    /**
     * Recorre al primer objeto de la lista
     */
    public void primero() {
        actual = 1;
        accionSeleccionar.ejecutar(actual);
    }

    /**
     * Recorre al último objeto de la lista
     */
    public void ultimo() {
        actual = getTotal();
        accionSeleccionar.ejecutar(actual);
    }

    /**
     * Obtiene la posición de la lista del actual objeto
     *
     * @return
     */
    public int getActual() {
        return actual;
    }

    /**
     * Cambia el actual objeto de acuerdo a su posición
     *
     * @param actual
     */
    public void setActual(int actual) {
        this.actual = actual;
        cleanActual();
    }

    /**
     * Devuelve el total de registros disponibles
     *
     * @return
     */
    public int getTotal() {
        total = (Integer) accionGetTotal.ejecutar();
        return total;
    }
}
