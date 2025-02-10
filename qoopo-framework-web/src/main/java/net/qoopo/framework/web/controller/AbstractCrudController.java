package net.qoopo.framework.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.data.repository.CrudRepository;
import net.qoopo.framework.web.components.nav.NavController;

/**
 * Controlador web para Jsf que gestiona los procesos de CRUD
 */
@Getter
@Setter
public abstract class AbstractCrudController<Entity, EntityData, EntityID> implements Serializable {

    protected static Logger log = Logger.getLogger("abstract-admin-controller");
    protected Entity objeto = null;
    protected List<EntityData> selectedData = new ArrayList<>();
    protected Iterable<EntityData> data = new ArrayList<>();
    protected CrudRepository<Entity, EntityID> repository;
    protected boolean editando;
    protected boolean masivo = false;

    protected NavController nav = new NavController(new Accion() {
        @Override
        public Object ejecutar(Object... parameters) {
            seleccionar((Integer) parameters[0]);
            return null;
        }
    }, new Accion() {
        @Override
        public Object ejecutar(Object... parameters) {
            return getTotal();
        }
    });

    public abstract void initObjeto();

    public abstract void loadData();

    /**
     * Método interno usado por los botones de navegación (anterior, siguiente,
     * etc)
     *
     * @param indice
     */
    public abstract void seleccionar(int indice);

    public abstract int getTotal();

    public void nuevo() {
        initObjeto();
        editando = false;
    }

    public boolean validateDelete(Entity item) {
        return true;
    }

    public void postDelete(Entity item) {
        //
    }

    public void save() {
        try {
            objeto = (Entity) repository.save(objeto);
            // loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Metodo generico que actualiza un objeto
     */
    public void update() {
        try {
            objeto = (Entity) repository.save(objeto);
            // loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void delete(Entity item) {
        try {
            if (validateDelete(item)) {
                repository.delete(item);
                postDelete(item);
                loadData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void delete() {
        delete(objeto);
    }

    public void edit(Entity item) {
        setObjeto(item);
        editando = true;
    }

    /**
     * Devuelve el objeto, si no se ha seleccionado ningún devuelve el primer
     * objeto de la lista, si no hay una lista crea un nuevo objeto listo para
     * ser guardado
     *
     * @return
     */
    public Entity getObjeto() {
        if (objeto == null) {
            if (getTotal() > 0) {
                this.seleccionar(1);
            } else {
                this.initObjeto();
            }
        }
        return objeto;
    }

}
