package net.qoopo.framework.filter;

import java.util.List;

import net.qoopo.framework.filter.core.Filter;

/**
 * Repositorio para los filtros que se implementen con el framework
 * 
 * Define los métodos que debe implementar in repositorio para filtros los
 * cuales son
 * 
 * 
 * public List<T> apply(Filter filtro);
 * 
 * public Long applyCount(Filter filtro);
 * 
 * public List<T> apply(Filter filtro, int first, int pageSize);
 * 
 * 
 */
public interface FilterRepository <T>{

    /**
     * Realiza el proceso del filtro indicado y devuelve un listado de las entidades
     * que cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @return
     */
    public List<T> apply(Filter filtro);

    /**
     * Realiza el proceso del filtro indicado y devuelte el número de registros que
     * cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @return
     */
    public Long applyCount(Filter filtro);

    /**
     * Realiza el proceso del filtro indicado y devuelve un listado de las entidades
     * que cumplieron las condiciones del filtro con parámetros de paginacion
     * 
     * @param filtro
     * @param first
     * @param pageSize
     * @return
     */
    public List<T> apply(Filter filtro, int first, int pageSize);

    /**
     * Realiza el proceso del filtro indicado y devuelte el número de registros que
     * cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @param first
     * @param pageSize
     * @return
     */
    public Long applyCount(Filter filtro, int first, int pageSize);
}
