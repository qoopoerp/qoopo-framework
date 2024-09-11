package net.qoopo.qoopoframework.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * Este bean maneja la cache de objetos para no tener que ser cargados de la
 * base de datos
 *
 * @author ALBERTO
 */
@Named
@ApplicationScoped
public class CacheBean implements Serializable {

    public static final Logger log = Logger.getLogger("Qoopo");

    private static final Map<Object, Object> CACHE = new HashMap<>();

    public CacheBean() {
        // constructor
    }

    public void vaciar() {
        CACHE.clear();
    }

    public Object obtener(Object clave) {
        try {
            return CACHE.get(clave);
        } catch (Exception e) {
            return null;
        }
    }

    public void poner(Object clave, Object valor) {
        CACHE.put(clave, valor);
        // this.imprimirTamanio();
    }

    public void imprimirTamanio() {
        log.log(Level.INFO, "Objetos en el cache {0}", CACHE.size());
    }

}
