package net.qoopo.framework.filter;

import java.util.Collections;
import java.util.List;

import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.data.jpa.Jpa;
import net.qoopo.framework.data.jpa.JpaParameters;
import net.qoopo.framework.data.jpa.JpaTransaction;

/**
 * Controlador DAO para los filtros <Filter>
 *
 * @author Alberto
 */
public class FilterDAO<T> {

    private Jpa<T, Long> jpa;

    public FilterDAO(Class<T> entityClass) {
        jpa = new Jpa<>(entityClass);
    }

    public List<T> apply(JpaTransaction transaccion, Filter filtro) {
        if (filtro != null)
            return jpa
                    .setEm(transaccion.getEm())
                    .setParam(UtilParameters.getJpaParameters(filtro, JpaParameters.get()))
                    .runQueryList(filtro.buildQuery());
        else {
            return Collections.EMPTY_LIST;
        }
    }

    public Long applyCount(JpaTransaction transaccion, Filter filtro) {
        if (filtro != null)
            return (Long) jpa
                    .setEm(transaccion.getEm())
                    .setParam(UtilParameters.getJpaParameters(filtro, JpaParameters.get()))
                    .runQuery(filtro.buildQueryCount());
        else {
            return 0L;
        }
    }

    public List<T> apply(JpaTransaction transaccion, Filter filtro, int first, int pageSize) {
        if (filtro != null)
            return jpa
                    .setEm(transaccion.getEm())
                    .setFirstResult(first)
                    .setMaxResults(pageSize)
                    .setParam(UtilParameters.getJpaParameters(filtro, JpaParameters.get()))
                    .runQueryList(filtro.buildQuery());
        else {
            return Collections.EMPTY_LIST;
        }
    }

    public Long applyCount(JpaTransaction transaccion, Filter filtro, int first, int pageSize) {
        if (filtro != null)
            return (Long) jpa
                    .setEm(transaccion.getEm())
                    .setFirstResult(first)
                    .setMaxResults(pageSize)
                    .setParam(UtilParameters.getJpaParameters(filtro, JpaParameters.get()))
                    .runQuery(filtro.buildQueryCount());
        else {
            return 0L;
        }
    }

}
