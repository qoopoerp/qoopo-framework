package net.qoopo.framework.exporter;

import java.io.OutputStream;

/**
 * Interface que deben implementar las utilidades de exportacion de registros
 * del sistema
 *
 * @author alberto
 */
public interface Exporter {

    public void clear();

    public void set(String campo, Object valor);

    public void set(Integer index, Object valor);

    /**
     * Metodo llamado al iniciar la escritura de un item
     */
    public void startItem();

    /**
     * Metodo llamado al terminar de escribir un item
     */
    public void endItem();

    public void setOutputExporter(OutputStream out);

    /**
     * Realiza la exportacion hacia el outputStream
     */
    public void exportar() throws Exception;

    public String getMimetype();

    public String getExtension();

}
