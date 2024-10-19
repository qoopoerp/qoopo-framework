package net.qoopo.qoopoframework.repository;

import java.util.logging.Logger;

import net.qoopo.qoopoframework.QoopoFramework;
import net.qoopo.qoopoframework.util.exceptions.QoopoException;
import net.qoopo.util.db.dao.GenericDAOSingleton;
import net.qoopo.util.db.jpa.JpaTransaction;
import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

/**
 * Utilitario que permite acceder a las funciones básicas de acceso a a base de
 * datos.
 * Hace uso de transacciones forzadas con los utilitarios
 * net.qoopo.util.db.jpa.JpaTransaction y
 * net.qoopo.util.db.daos.GenericDAOSingleton
 * 
 * 
 * - create
 * - createAll
 * - edit
 * - editAll
 * - find
 * - delete
 * - deleteAll
 * 
 * @author Alberto García
 */
public class QoopoJpaRepositorySingleton {

    public static final Logger log = Logger.getLogger("qoopo-framework-qoopojparepository");

    /**
     * Crea una lista de entidades en una sola transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public static void createAll(Iterable item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
        trx.begin();
        try {
            for (Object t : item) {
                GenericDAOSingleton.create(trx, t);
            }
            trx.commit();
        } catch (RollbackFailureException | QoopoException e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Crea una entidad
     *
     * @param item
     * @return
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     */
    public static Object create(Object item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return null;
        }
        JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
        trx.begin();
        try {
            GenericDAOSingleton.create(trx, item);
            trx.commit();
            /*
             * } catch (ConstraintViolationException e) {
             * log.log(Level.SEVERE, "Exception Constraints: ");
             * e.getConstraintViolations().forEach(err -> log.log(Level.SEVERE,
             * err.toString()));
             * trx.rollback();
             * throw e;
             */
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
        return item;
    }

    /**
     * Crea una entidad sin lanzar excepcion
     *
     * @param item
     * @return
     */
    public static Object createWithoutError(Object item) {
        try {
            if (item == null) {
                return null;
            }
            JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
            trx.begin();
            try {
                GenericDAOSingleton.create(trx, item);
                trx.commit();
            } catch (Exception e) {
                trx.rollback();
            }
            return item;
        } catch (Exception ex) {
            //
        }
        return null;
    }

    /**
     *
     * @param entityClass
     * @param id
     * @return
     */
    public static Object find(Class entityClass, Long id) {
        JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
        trx.open();
        Object item = null;
        try {
            item = GenericDAOSingleton.find(trx, entityClass, id);
            trx.close();
        } catch (Exception e) {
            trx.close();
        }
        return item;
    }

    /**
     * Edita una lista de entidades en una sola transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    public static void editAll(Iterable item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
        trx.begin();
        try {
            for (Object t : item) {
                GenericDAOSingleton.edit(trx, t);
            }
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Edita una conciliación
     *
     * @param item
     * @return
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    public static Object edit(Object item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return null;
        }
        JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
        trx.begin();
        try {
            item = GenericDAOSingleton.edit(trx, item);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
        return item;
    }

    /**
     * Elimina una lista de entidades en una transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    public static void deleteAll(Iterable item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
        trx.begin();
        try {
            for (Object t : item) {
                GenericDAOSingleton.delete(trx, t);
            }
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Elimina una entidad
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
     * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
     */
    public static void delete(Object item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
        trx.begin();
        try {
            GenericDAOSingleton.delete(trx, item);
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            throw e;
        }
    }

    /**
     * Elimina una entidad sin lanzar exception
     *
     * @param item
     *
     */
    public static void deleteSilent(Object item) {
        try {
            JpaTransaction trx = JpaTransaction.get(QoopoFramework.get().getDataSourceName());
            trx.begin();
            try {
                GenericDAOSingleton.delete(trx, item);
                trx.commit();
            } catch (IllegalOrphanException | NonexistentEntityException | RollbackFailureException
                    | QoopoException e) {
                trx.rollback();
            }
        } catch (Exception ex) {
            //
        }
    }

    private QoopoJpaRepositorySingleton() {
        //
    }

}
