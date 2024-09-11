package net.qoopo.qoopoframework.web.core.interfaces;

/**
 * Interface que debe implementar los beans que manejan un proceso que requiera
 * una barra de progreso
 *
 * @author alberto
 */
public interface AdminBeanProgressable {

    /**
     * Devuelve el procentaje del progreso
     *
     * @return
     */
    public Integer getProgress();

    /**
     * Devuelve el status para mostrar en el progreso
     *
     * @return
     */
    public String getProgressStatus();

    // /**
    // * Permite actualizar el porcentaje
    // * @param progress
    // */
    // public void setProgress(Integer progress);
    /**
     * Metodo llamado cuanto el proceso finalice
     */
    public void onComplete();

}
