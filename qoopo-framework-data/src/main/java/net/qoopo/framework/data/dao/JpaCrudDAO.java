package net.qoopo.framework.data.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.qoopo.framework.data.jpa.Jpa;
import net.qoopo.framework.data.jpa.exceptions.IllegalOrphanException;
import net.qoopo.framework.data.jpa.exceptions.NonexistentEntityException;
import net.qoopo.framework.data.jpa.exceptions.RollbackFailureException;

/**
 * Clase que permite realiza las operaciones basicas CRUD sobre cualquier
 * entidad de la base de datos
 *
 * @author alberto
 */
public class JpaCrudDAO<T, ID> implements CrudDAO<T, ID> {

    private Jpa<T, ID> jpa;

    private Class<T> entityClass = null;
    private EntityManagerFactory emf = null;
    private EntityManager em = null;

    // public JpaCrudDAO() {
    // jpa = new Jpa<>();
    // }

    public JpaCrudDAO(Class<T> entityClass) {
        jpa = new Jpa<>(entityClass);
        this.entityClass = entityClass;
    }

    public JpaCrudDAO(Class<T> entityClass, EntityManagerFactory emf) {
        jpa = new Jpa<>(entityClass);
        this.entityClass = entityClass;
        this.emf = emf;
    }

    public JpaCrudDAO(Class<T> entityClass, EntityManager em) {
        jpa = new Jpa<>(entityClass);
        this.entityClass = entityClass;
        this.em = em;
    }

    public JpaCrudDAO(EntityManagerFactory emf) {
        jpa = new Jpa<>(entityClass);
        this.emf = emf;
    }

    public JpaCrudDAO(EntityManager em) {
        jpa = new Jpa<>(entityClass);
        this.em = em;
    }

    private EntityManager getEntityManager() {
        if (em == null)
            return emf.createEntityManager();
        else
            return em;
    }

    @Override
    public T create(T item) throws Exception, RollbackFailureException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        EntityManager em = getEntityManager();
        T returnValue = null;
        try {
            em.getTransaction().begin();
            returnValue = jpa.setEm(em).setEntityClass(entityClass).create(item);
            em.getTransaction().commit();
            return returnValue;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }

    }

    @Override
    public T edit(T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();

        EntityManager em = getEntityManager();
        T returnValue = null;
        try {
            em.getTransaction().begin();
            returnValue = jpa.setEm(em).setEntityClass(entityClass).edit(item);
            em.getTransaction().commit();
            return returnValue;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(T item)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        if (entityClass == null)
            entityClass = (Class<T>) item.getClass();
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            jpa.setEm(em).setEntityClass(entityClass).delete(item);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deletebyId(ID id)
            throws Exception, NonexistentEntityException, RollbackFailureException, IllegalOrphanException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            jpa.setEm(em).setEntityClass(entityClass).deletebyId(id);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }

    }

    @Override
    public Optional<T> find(ID id) {
        return jpa.setEm(getEntityManager()).setEntityClass(entityClass).find(id);
    }

    @Override
    public List<T> findAll() {
        return jpa.setEm(getEntityManager()).setEntityClass(entityClass).findEntities();
    }

    @Override
    public List<T> findAll(int maxResults, int firstResult) {
        return jpa.setEm(getEntityManager()).setEntityClass(entityClass).findEntities(maxResults,
                firstResult);
    }

}
