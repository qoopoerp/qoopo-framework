package net.qoopo.framework.resilience.queue;

public interface ResilientQueue<TYPE> {

    /**
     * Agrega un item a la cola
     * 
     * @param item
     */
    public void add(TYPE item);

    /**
     * Obtiene y remueve el primer item de la cola
     * 
     * @return
     */
    public TYPE poll();

    /**
     * Carga los items de la cola desde el repositorio
     */
    public void load();

    /**
     * Limpia la cola
     */
    public void clear();

    /**
     * Obtiene el tamaño de la cola
     * 
     * @return
     */
    public int getSize();

    /**
     * Obtiene los items de la cola
     * 
     * @return
     */
    public Iterable<TYPE> getItems();

}
