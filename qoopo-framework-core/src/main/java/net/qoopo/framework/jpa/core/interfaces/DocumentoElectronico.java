package net.qoopo.framework.jpa.core.interfaces;

/**
 * Esta interfaz es para las entidades que deben tener campos de documento
 * electronico.
 *
 * @author alberto
 */
public interface DocumentoElectronico {

    public boolean isFacturaElectronica();

    public void setFacturaElectronica(boolean facturaElectronica);

    public boolean isFeAutorizado();

    public void setFeAutorizado(boolean feAutorizado);

    public String getFeAutorizacion();

    public void setFeAutorizacion(String feAutorizacion);

    public String getFeClaveAcceso();

    public void setFeClaveAcceso(String feClaveAcceso);

    public String getFeEstado();

    public void setFeEstado(String feEstado);
}
