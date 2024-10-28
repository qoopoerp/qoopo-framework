package net.qoopo.framework.proceso.cola;

import java.io.Serializable;

import net.qoopo.framework.Accion;

/**
 *
 * @author alberto
 */
public class ItemCola implements Serializable {

    private Accion accionOK;
    private Accion accionError;

    public ItemCola() {
    }

    public ItemCola(Accion accion) {
        this.accionOK = accion;
    }

    public ItemCola(Accion accionOK, Accion accionError) {
        this.accionOK = accionOK;
        this.accionError = accionError;
    }
        
    public Accion getAccionOK() {
        return accionOK;
    }

    public void setAccionOK(Accion accionOK) {
        this.accionOK = accionOK;
    }

    public Accion getAccionError() {
        return accionError;
    }

    public void setAccionError(Accion accionError) {
        this.accionError = accionError;
    }

}
