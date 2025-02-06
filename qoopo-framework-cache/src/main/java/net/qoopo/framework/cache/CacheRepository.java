package net.qoopo.framework.cache;

/**
 * Define un repositorio de cache donde se almacena en el Formato Clave-valor
 */
public interface CacheRepository<KEY, VALUE> {

    /**
     * Guarda un registro
     * 
     * @param key
     * @param value
     */
    public void save(KEY key, VALUE value);

    /**
     * Recupera el registro almacenado
     * 
     * @param key
     * @return
     */
    public VALUE get(String key);

    public void clear();
}
