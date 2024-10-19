package net.qoopo.qoopoframework.repository;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import net.qoopo.qoopoframework.jpa.filter.Filter;

/**
 * Repositorio para los filtros que se implementen con el framework
 * 
 * Define los métodos que debe implementar in repositorio para filtros los
 * cuales son
 * 
 * 
 * public List<T> filtrar(Filter filtro);
 * 
 * public Long filtrarCount(Filter filtro);
 * 
 * public List<T> filtrar(Filter filtro, int first, int pageSize);
 * 
 * 
 */
public interface IFilterRepository {

    /**
     * Realiza el proceso del filtro indicado y devuelve un listado de las entidades
     * que cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @return
     */
    public List<T> filtrar(Filter filtro);

    /**
     * Realiza el proceso del filtro indicado y devuelte el número de registros que
     * cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @return
     */
    public Long filtrarCount(Filter filtro);

    /**
     * Realiza el proceso del filtro indicado y devuelve un listado de las entidades
     * que cumplieron las condiciones del filtro con parámetros de paginacion
     * 
     * @param filtro
     * @param first
     * @param pageSize
     * @return
     */
    public List<T> filtrar(Filter filtro, int first, int pageSize);

    /**
     * Realiza el proceso del filtro indicado y devuelte el número de registros que
     * cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @param first
     * @param pageSize
     * @return
     */
    public Long filtrarCount(Filter filtro, int first, int pageSize);
}
