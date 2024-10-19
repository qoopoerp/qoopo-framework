package net.qoopo.qoopoframework.repository;

import net.qoopo.qoopoframework.QoopoFramework;
import net.qoopo.util.db.repository.JpaRepository;

/**
 * Utilitario que permite acceder a las funciones básicas de acceso a a base de
 * datos.
 * Hace uso de transacciones forzadas con los utilitarios
 * net.qoopo.util.db.jpa.JpaTransaction y net.qoopo.util.db.daos.GenericDAO
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
public class QoopoJpaRepository<T, ID> extends JpaRepository<T, ID> {

    public QoopoJpaRepository() {
        super(QoopoFramework.get().getDataSourceName());
    }

    public QoopoJpaRepository(Class<T> entityClass) {
        super(entityClass, QoopoFramework.get().getDataSourceName());
    }

}
