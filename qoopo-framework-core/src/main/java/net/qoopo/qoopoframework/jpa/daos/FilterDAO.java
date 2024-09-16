package net.qoopo.qoopoframework.jpa.daos;

import java.util.Collections;
import java.util.List;

import net.qoopo.qoopoframework.jpa.filter.Filter;
import net.qoopo.util.db.jpa.JPA;
import net.qoopo.util.db.jpa.Parametro;
import net.qoopo.util.db.jpa.Transaccion;

/**
 * Transacciones para los filtros en general
 *
 * @author Alberto
 */
public class FilterDAO {

    private FilterDAO() {
        //
    }

    public static List<Object> filtrar(Transaccion transaccion, Filter filtro) {
        if (filtro != null)
            return JPA.get()
                    .setEm(transaccion.getEm())
                    .setParam(filtro.obtenerParametros(Parametro.get()))
                    .ejecutarQueryList(filtro.buildQuery());
        else {
            return Collections.EMPTY_LIST;
        }
    }

    public static Long filtrarCount(Transaccion transaccion, Filter filtro) {
        return (Long) JPA.get()
                .setEm(transaccion.getEm())
                .setParam(filtro.obtenerParametros(Parametro.get()))
                .ejecutarQuery(filtro.buildQueryCount());
    }

    public static List<Object> filtrar(Transaccion transaccion, Filter filtro, int first, int pageSize) {
        return JPA.get()
                .setEm(transaccion.getEm())
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .setParam(filtro.obtenerParametros(Parametro.get()))
                .ejecutarQueryList(filtro.buildQuery());
    }

    public static Long filtrarCount(Transaccion transaccion, Filter filtro, int first, int pageSize) {
        return (Long) JPA.get()
                .setEm(transaccion.getEm())
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .setParam(filtro.obtenerParametros(Parametro.get()))
                .ejecutarQuery(filtro.buildQueryCount());
    }

}
