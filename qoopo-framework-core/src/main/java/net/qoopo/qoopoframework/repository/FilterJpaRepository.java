package net.qoopo.qoopoframework.repository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.qoopoframework.QoopoFramework;
import net.qoopo.qoopoframework.jpa.daos.FilterDAO;
import net.qoopo.qoopoframework.jpa.filter.Filter;
import net.qoopo.qoopoframework.util.QLogger;
import net.qoopo.util.db.jpa.Transaccion;

public class FilterJpaRepository {

    public static final Logger log = Logger.getLogger("Qoopo");

    private FilterJpaRepository() {
        //
    }

    public static List filtrar(Filter filtro) {
        long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        List<Object> valor = null;
        try {
            valor = FilterDAO.filtrar(trx, filtro);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            trx.cerrar();
        }
        log.log(Level.INFO, "Filtro [{0}] -- Terminado {1}",
                new Object[] { filtro.getNombre() + " (" + filtro.getTablaJPL() + ")",
                        QLogger.getTime(inicial) });
        return valor;
    }

    public static Long filtrarCount(Filter filtro) {
        long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        Long valor = null;
        try {
            valor = FilterDAO.filtrarCount(trx, filtro);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            trx.cerrar();
        }
        log.log(Level.INFO, "Filtro Count [{0}] -- Terminado {1}",
                new Object[] { filtro.getNombre() + " (" + filtro.getTablaJPL() + ")", QLogger.getTime(inicial) });
        return valor;
    }

    public static List filtrar(Filter filtro, int first, int pageSize) {
        long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        List<Object> valor = null;
        try {
            valor = FilterDAO.filtrar(trx, filtro, first, pageSize);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            trx.cerrar();
        }
        log.log(Level.INFO, "Filtro [{0}] -- Terminado {1}",
                new Object[] { filtro.getNombre() + " (" + filtro.getTablaJPL() + ")", QLogger.getTime(inicial) });
        return valor;
    }

    public static Long filtrarCount(Filter filtro, int first, int pageSize) {
        // long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        Long valor = null;
        try {
            valor = FilterDAO.filtrarCount(trx, filtro, first, pageSize);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            trx.cerrar();
        }
        // log.log(Level.INFO, "Filtro Count [{0}] -- Terminado {1}", new
        // Object[]{filtro.getNombre() + " (" + filtro.getTablaJPL() + ")",
        // QLogger.getTime(inicial)});
        return valor;
    }

}
