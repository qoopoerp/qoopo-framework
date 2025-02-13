package net.qoopo.framework.data.jpa;

import java.io.Closeable;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Maneja las transacciones
 *
 * @author Alberto
 */
public class JpaTransaction implements Closeable {

    private String dataSourceName = "";
    public static final Logger log = Logger.getLogger("transaction");
    //
    // http://docs.jboss.org/hibernate/stable/entitymanager/reference/en/html/transactions.html
    // según la pagina superior, indica que nunca hay que usar un entitymanager por
    // aplicación ni uno entitymanager por sesion de usuario
    private EntityManagerFactory emf;

    public static JpaTransaction get(String dataSourceName) {
        return new JpaTransaction(dataSourceName);
    }

    public EntityManager getEntityManager(String dataSourceName) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(dataSourceName);
        }

        if (emf != null) {
            return emf.createEntityManager();
        } else {
            log.severe("NO SE HA CREADO EN ENTITY MANAGER FACTORY, DEVUELVO NULO AL ENTITY MANAGER");
            return null;
        }
    }

    private EntityManager em;

    private JpaTransaction(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * Abre una sesion para la conexion
     */
    public void open() {
        em = getEntityManager(dataSourceName);
    }

    /**
     * Abre una sesion y crea una transaccion
     *
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void begin() throws Exception {
        open();
        em.getTransaction().begin();
    }

    /**
     * Termina una sesion y una transaccion
     *
     * @throws net.qoopo.qoopo.core.util.exceptions.QoopoException
     */
    public void commit() throws Exception {
        if (em != null) {
            try {
                em.getTransaction().commit();
            } finally {
                close();
            }
        }
    }

    /**
     * Cancela la transaccion y cierra la sesion
     *
     */
    public void rollback() {
        if (em != null) {
            try {
                em.getTransaction().rollback();
            } finally {
                close();
            }
        }
    }

    /**
     * Cierra la sesion
     */
    public void close() {
        if (em != null) {
            if (em.isOpen()) {
                em.close();
            } else {
                log.severe("Se intentó cerrar un entitymanager ya cerrado");
            }
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
