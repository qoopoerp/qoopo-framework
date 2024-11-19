package net.qoopo.framework.filter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.framework.data.jpa.JpaTransaction;
import net.qoopo.framework.filter.core.Filter;

/**
 * Repositorio para los filtros que se implementen con el framework
 */
public class FilterJpaRepository<T> implements FilterRepository<T> {

    public static final Logger log = Logger.getLogger("Qoopo");

    private FilterDAO<T> dao;

    private String dataSourceName;

    public FilterJpaRepository(String dataSourceName, Class<T> entityClass) {
        dao = new FilterDAO<T>(entityClass);
        this.dataSourceName = dataSourceName;
    }

    /**
     * Realiza el proceso del filtro indicado y devuelve un listado de las entidades
     * que cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @return
     */
    public List<T> apply(Filter filtro) {
        long inicial = System.currentTimeMillis();
        JpaTransaction trx = JpaTransaction.get(dataSourceName);
        trx.open();
        List<T> valor = null;
        try {
            valor = (List<T>) dao.apply(trx, filtro);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            trx.close();
        }
        log.log(Level.INFO, "Filtro [{0}] -- Terminado {1}",
                new Object[] { filtro.getName() + " (" + filtro.getCollection() + ")",
                        Util.getTime(inicial) });
        return valor;
    }

    /**
     * Realiza el proceso del filtro indicado y devuelte el número de registros que
     * cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @return
     */
    public Long applyCount(Filter filtro) {
        long inicial = System.currentTimeMillis();
        JpaTransaction trx = JpaTransaction.get(dataSourceName);
        trx.open();
        Long valor = null;
        try {
            valor = dao.applyCount(trx, filtro);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            trx.close();
        }
        log.log(Level.INFO, "Filtro Count [{0}] -- Terminado {1}",
                new Object[] { filtro.getName() + " (" + filtro.getCollection() + ")", Util.getTime(inicial) });
        return valor;
    }

    /**
     * Realiza el proceso del filtro indicado y devuelve un listado de las entidades
     * que cumplieron las condiciones del filtro con parámetros de paginacion
     * 
     * @param filtro
     * @param first
     * @param pageSize
     * @return
     */
    public List<T> apply(Filter filtro, int first, int pageSize) {
        long inicial = System.currentTimeMillis();
        JpaTransaction trx = JpaTransaction.get(dataSourceName);
        trx.open();
        List<T> valor = null;
        try {
            valor = (List<T>) dao.apply(trx, filtro, first, pageSize);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            trx.close();
        }
        log.log(Level.INFO, "Filtro [{0}] -- Terminado {1}",
                new Object[] { filtro.getName() + " (" + filtro.getCollection() + ")", Util.getTime(inicial) });
        return valor;
    }

    /**
     * Realiza el proceso del filtro indicado y devuelte el número de registros que
     * cumplieron las condiciones del filtro
     * 
     * @param filtro
     * @param first
     * @param pageSize
     * @return
     */
    public Long applyCount(Filter filtro, int first, int pageSize) {
        long inicial = System.currentTimeMillis();
        JpaTransaction trx = JpaTransaction.get(dataSourceName);
        trx.open();
        Long valor = null;
        try {
            valor = dao.applyCount(trx, filtro, first, pageSize);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            trx.close();
        }
        log.log(Level.INFO, "Filtro Count [{0}] -- Terminado {1}",
                new Object[] { filtro.getName() + " (" + filtro.getCollection() + ")",
                        Util.getTime(inicial) });
        return valor;
    }

}
