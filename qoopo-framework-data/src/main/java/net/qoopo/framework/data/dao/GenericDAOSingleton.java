package net.qoopo.framework.data.dao;

import net.qoopo.framework.data.jpa.Jpa;
import net.qoopo.framework.data.jpa.JpaTransaction;
import net.qoopo.framework.data.jpa.exceptions.IllegalOrphanException;
import net.qoopo.framework.data.jpa.exceptions.NonexistentEntityException;
import net.qoopo.framework.data.jpa.exceptions.RollbackFailureException;

/**
 * Clase que permite realiza las operaciones basicas CRUD sobre cualquier
 * entidad de la base de datos
 *
 * @author alberto
 */
public class GenericDAOSingleton {

    public static Object create(JpaTransaction transaccion, Object item) throws Exception, RollbackFailureException {
        return Jpa.get().setEm(transaccion.getEm()).setEntityClass(item.getClass()).create(item);
    }

    public static Object edit(JpaTransaction transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        return Jpa.get().setEm(transaccion.getEm()).setEntityClass(item.getClass()).edit(item);
    }

    public static void delete(JpaTransaction transaccion, Object item) throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        Jpa.get().setEm(transaccion.getEm()).setEntityClass(item.getClass()).delete(item);
    }

    public static Object find(JpaTransaction transaccion, Class entityClass, Long id) {
        return Jpa.get().setEm(transaccion.getEm()).setEntityClass(entityClass).find(id).get();
    }

}
