package net.qoopo.framework.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Realiza una implementación de cache en memoria en un Mapa estatico
 */
public class CacheSingletonRepository implements CacheRepository<String, Object> {

    private static Map<String, Object> storage = new HashMap<>();

    private static final CacheSingletonRepository INSTANCE = new CacheSingletonRepository();

    public static CacheSingletonRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(String key, Object value) {
        storage.put(key, value);
    }

    @Override
    public Object get(String key) {
        return storage.get(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

}
