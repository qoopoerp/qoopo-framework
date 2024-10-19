package net.qoopo.qoopoframework.jpa.daos;

import java.util.Collections;
import java.util.List;

import net.qoopo.qoopoframework.jpa.filter.Filter;
import net.qoopo.util.db.jpa.JPA;
import net.qoopo.util.db.jpa.Parametro;
import net.qoopo.util.db.jpa.JpaTransaction;

/**
 * Controlador DAO para los filtros <Filter>
 *
 * @author Alberto
 */
public class FilterDAO<T> {

    private JPA<T, Long> jpa;

    public FilterDAO() {
        jpa = new JPA<>();
    }

    public List<T> filtrar(JpaTransaction transaccion, Filter filtro) {
        if (filtro != null)
            return jpa
                    .setEm(transaccion.getEm())
                    .setParam(filtro.obtenerParametros(Parametro.get()))
                    .ejecutarQueryList(filtro.buildQuery());
        else {
            return Collections.EMPTY_LIST;
        }
    }

    public Long filtrarCount(JpaTransaction transaccion, Filter filtro) {
        return (Long) jpa
                .setEm(transaccion.getEm())
                .setParam(filtro.obtenerParametros(Parametro.get()))
                .ejecutarQuery(filtro.buildQueryCount());
    }

    public List<T> filtrar(JpaTransaction transaccion, Filter filtro, int first, int pageSize) {
        return jpa
                .setEm(transaccion.getEm())
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .setParam(filtro.obtenerParametros(Parametro.get()))
                .ejecutarQueryList(filtro.buildQuery());
    }

    public Long filtrarCount(JpaTransaction transaccion, Filter filtro, int first, int pageSize) {
        return (Long) jpa
                .setEm(transaccion.getEm())
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .setParam(filtro.obtenerParametros(Parametro.get()))
                .ejecutarQuery(filtro.buildQueryCount());
    }

}
