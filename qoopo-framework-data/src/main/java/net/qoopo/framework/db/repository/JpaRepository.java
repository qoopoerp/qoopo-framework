package net.qoopo.framework.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.qoopo.framework.db.dao.JpaCrudDAO;
import net.qoopo.framework.db.jpa.exceptions.IllegalOrphanException;
import net.qoopo.framework.db.jpa.exceptions.NonexistentEntityException;
import net.qoopo.framework.db.jpa.exceptions.RollbackFailureException;

/**
 * Repositorio JPA para las operaciones CRUD
 */
public class JpaRepository<T, ID> implements CrudRepository<T, ID> {
    public static final Logger log = Logger.getLogger("jparepository");

    protected JpaCrudDAO<T, ID> crudDao;
    protected String datasourceName;
    protected EntityManagerFactory emf;

    public JpaRepository(String datasourceName) {
        emf = Persistence.createEntityManagerFactory(datasourceName);
        crudDao = new JpaCrudDAO<>(emf);
        this.datasourceName = datasourceName;
    }

    public JpaRepository(Class<T> entityClass, String datasourceName) {
        emf = Persistence.createEntityManagerFactory(datasourceName);
        crudDao = new JpaCrudDAO<>(entityClass, emf);
        this.datasourceName = datasourceName;
    }

    /**
     * Crea una lista de entidades en una sola transaccion
     *
     * @param item
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void saveAll(Iterable<T> item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return;
        }
        for (T t : item) {
            crudDao.create(t);
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
    @Override
    public T save(T item)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        if (item == null) {
            return null;
        }
        item = crudDao.edit(item);
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
    @Override
    public void deleteAll(Iterable<T> item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }

        for (T t : item) {
            crudDao.delete(t);
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
    @Override
    public void delete(T item)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (item == null) {
            return;
        }
        crudDao.delete(item);
    }

    @Override
    public void deleteById(ID id)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException, Exception {
        if (id == null) {
            return;
        }
        crudDao.deletebyId(id);
    }

    /**
     *
     * @param entityClass
     * @param id
     * @return
     */
    @Override
    public Optional<T> find(ID id) {
        return crudDao.find(id);
    }

    @Override
    public List<T> findAll() {
        return crudDao.findAll();
    }

    @Override
    public List<T> findAll(int maxResults, int firstResult) {
        return crudDao.findAll(maxResults, firstResult);
    }
}
