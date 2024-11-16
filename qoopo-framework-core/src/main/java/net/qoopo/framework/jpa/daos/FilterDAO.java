package net.qoopo.framework.jpa.daos;

import java.util.Collections;
import java.util.List;

import net.qoopo.framework.data.jpa.Jpa;
import net.qoopo.framework.data.jpa.JpaTransaction;
import net.qoopo.framework.data.jpa.JpaParameters;
import net.qoopo.framework.jpa.filter.Filter;

/**
 * Controlador DAO para los filtros <Filter>
 *
 * @author Alberto
 */
public class FilterDAO<T> {

    private Jpa<T, Long> jpa;

    public FilterDAO() {
        jpa = new Jpa<>();
    }

    public List<T> filtrar(JpaTransaction transaccion, Filter filtro) {
        if (filtro != null)
            return jpa
                    .setEm(transaccion.getEm())
                    .setParam(filtro.obtenerJpaParameterss(JpaParameters.get()))
                    .runQueryList(filtro.buildQuery());
        else {
            return Collections.EMPTY_LIST;
        }
    }

    public Long filtrarCount(JpaTransaction transaccion, Filter filtro) {
        return (Long) jpa
                .setEm(transaccion.getEm())
                .setParam(filtro.obtenerJpaParameterss(JpaParameters.get()))
                .runQuery(filtro.buildQueryCount());
    }

    public List<T> filtrar(JpaTransaction transaccion, Filter filtro, int first, int pageSize) {
        return jpa
                .setEm(transaccion.getEm())
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .setParam(filtro.obtenerJpaParameterss(JpaParameters.get()))
                .runQueryList(filtro.buildQuery());
    }

    public Long filtrarCount(JpaTransaction transaccion, Filter filtro, int first, int pageSize) {
        return (Long) jpa
                .setEm(transaccion.getEm())
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .setParam(filtro.obtenerJpaParameterss(JpaParameters.get()))
                .runQuery(filtro.buildQueryCount());
    }

}
