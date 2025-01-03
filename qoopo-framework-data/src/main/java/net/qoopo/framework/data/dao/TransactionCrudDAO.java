package net.qoopo.framework.data.dao;

import java.util.List;
import java.util.Optional;

import net.qoopo.framework.data.jpa.JpaTransaction;
import net.qoopo.framework.data.jpa.exceptions.IllegalOrphanException;
import net.qoopo.framework.data.jpa.exceptions.NonexistentEntityException;
import net.qoopo.framework.data.jpa.exceptions.RollbackFailureException;

/**
 * Clase que permite realiza las operaciones basicas CRUD sobre cualquier
 * entidad de la base de datos con el manejo explicito de las transacciones
 *
 * @author alberto
 */
public interface TransactionCrudDAO<T, ID> {

        public T create(JpaTransaction transaccion, T item) throws Exception, RollbackFailureException;

        public T edit(JpaTransaction transaccion, T item)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public void deletebyId(JpaTransaction transaccion, ID id)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public void delete(JpaTransaction transaccion, T item)
                        throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException;

        public Optional<T> find(JpaTransaction transaccion, ID id);

        public List<T> findAll(JpaTransaction transaccion);

        public List<T> findAll(JpaTransaction transaccion, int maxResults, int firstResult);

}
