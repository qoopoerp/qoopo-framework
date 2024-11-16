package net.qoopo.framework.data.repository;

import java.util.Optional;

import net.qoopo.framework.data.jpa.exceptions.IllegalOrphanException;
import net.qoopo.framework.data.jpa.exceptions.NonexistentEntityException;
import net.qoopo.framework.data.jpa.exceptions.RollbackFailureException;

/**
 * Repositoio para las operaciones CRUD Básicas
 */
public interface CrudRepository<T, ID> {

        /**
         * Save an array of entities
         *
         * @param item
         * 
         */
        public void saveAll(Iterable<T> item)
                        throws NonexistentEntityException, RollbackFailureException, Exception;

        /**
         * Save a entity
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
         * find a entity by id
         * 
         * @param entityClass
         * @param id
         * @return
         */
        public Optional<T> find(ID id);

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
         * Delete an array of entities
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
         * Delete an entity
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
         * Delete an entity by Id
         * 
         * @param item
         * @throws NonexistentEntityException
         * @throws RollbackFailureException
         * @throws IllegalOrphanException
         * @throws Exception
         */
        public void deleteById(ID item)
                        throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception;

}
