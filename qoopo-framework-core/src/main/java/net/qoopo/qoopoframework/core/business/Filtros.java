package net.qoopo.qoopoframework.core.business;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.qoopoframework.core.QoopoFramework;
import net.qoopo.qoopoframework.core.db.daos.FiltrosDAO;
import net.qoopo.qoopoframework.core.db.filtro.Filtro;
import net.qoopo.qoopoframework.core.util.QLogger;
import net.qoopo.util.db.jpa.Transaccion;

public class Filtros {

    public static final Logger log = Logger.getLogger("Qoopo");

    private Filtros() {
        //
    }

    public static List filtrar(Filtro filtro) {
        long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        List<Object> valor = null;
        try {
            valor = FiltrosDAO.filtrar(trx, filtro);
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

    public static Long filtrarCount(Filtro filtro) {
        long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        Long valor = null;
        try {
            valor = FiltrosDAO.filtrarCount(trx, filtro);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            trx.cerrar();
        }
        log.log(Level.INFO, "Filtro Count [{0}] -- Terminado {1}",
                new Object[] { filtro.getNombre() + " (" + filtro.getTablaJPL() + ")", QLogger.getTime(inicial) });
        return valor;
    }

    public static List filtrar(Filtro filtro, int first, int pageSize) {
        long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        List<Object> valor = null;
        try {
            valor = FiltrosDAO.filtrar(trx, filtro, first, pageSize);
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

    public static Long filtrarCount(Filtro filtro, int first, int pageSize) {
        // long inicial = System.currentTimeMillis();
        Transaccion trx = Transaccion.get(QoopoFramework.get().getDataSourceName());
        trx.abrir();
        Long valor = null;
        try {
            valor = FiltrosDAO.filtrarCount(trx, filtro, first, pageSize);
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
