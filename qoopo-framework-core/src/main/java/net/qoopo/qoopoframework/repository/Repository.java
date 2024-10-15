package net.qoopo.qoopoframework.repository;

import java.util.Optional;

import net.qoopo.util.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.util.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.util.db.jpa.exceptions.RollbackFailureException;

/**
 * Repositorio donde se debe almacenar la información
 */
public interface Repository<T, S> {

        /**
         * Guarda una lista
         *
         * @param item
         * 
         */
        public void saveAll(Iterable<T> item)
                        throws NonexistentEntityException, RollbackFailureException, Exception;

        /**
         * Guarda
         *
         * @param item
         * @return
         * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
         */
        public T save(T item)
                        throws NonexistentEntityException, RollbackFailureException, Exception;

        /**
         *
         * @param entityClass
         * @param id
         * @return
         */
        public Optional<T> find(S id);

        /**
         * Return all entities
         * 
         * @return
         */
        public Iterable<T> findAll();

        /**
         * Return all entities paged
         * 
         * @param maxResults
         * @param firstResult
         * @return
         */
        public Iterable<T> findAll(int maxResults, int firstResult);

        /**
         * Elimina una lista de entidades en una transaccion
         *
         * @param item
         * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
         */
        public void deleteAll(Iterable<T> item)
                        throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

        /**
         * Elimina una entidad
         *
         * @param item
         * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.NonexistentEntityException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.RollbackFailureException
         * @throws net.qoopo.qoopo.core.db.jpa.exceptions.IllegalOrphanException
         */
        public void delete(T item)
                        throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

        /**
         * Elimina una entidad a partir del id
         * 
         * @param item
         * @throws NonexistentEntityException
         * @throws RollbackFailureException
         * @throws IllegalOrphanException
         * @throws Exception
         */
        public void deleteById(S item)
                        throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

}
