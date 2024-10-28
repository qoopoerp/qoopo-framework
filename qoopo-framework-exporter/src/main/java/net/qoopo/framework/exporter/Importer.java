package net.qoopo.framework.exporter;

import java.io.InputStream;

/**
 * Interface que deben implementar las utilidades que permitan realizar la
 * importacion de registros del sistema
 *
 * @author alberto
 */
public interface Importer {

    public Object get(String campo);

    public Object get(Integer index);

    public Integer getCurrent();

    public Integer getTotal();

    /**
     * Metodo llamado al iniciar la lectura de un item
     */
    public void startItem();

    /**
     * Metodo llamado al finalizar de leer un item
     */
    public void endItem();

    public void setInputStream(InputStream in);

    /**
     * Realiza la importacion a partir del inputStream
     */
    public void importar() throws Exception;

    /**
     * Indica si aun hay datos por leer
     *
     * @return
     */
    public boolean hasItems();

}
