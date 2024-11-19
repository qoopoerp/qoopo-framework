package net.qoopo.framework.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.data.repository.CrudRepository;

/**
 * Controlador web para Jsf que gestiona los procesos de CRUD
 */
@Getter
@Setter
public abstract class AbstractAdminController<Entity, EntityData, EntityID> implements Serializable {

    protected static Logger log = Logger.getLogger("abstract-admin-controller");
    protected Entity objeto = null;
    protected List<EntityData> listaSeleccionados = new ArrayList<>();
    protected Iterable<EntityData> data = new ArrayList<>(); // en los beans que tienen carga diferida, este corresponde
                                                             // a los

    protected CrudRepository<Entity, EntityID> repository;

    protected boolean editando;
    protected boolean masivo = false;

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
            loadData();
            // edit(objeto);
        } catch (Exception ex) {
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
            // Optional<T> tmp = repository.find(objeto.getId());
            // if (tmp.isPresent())
            // objeto = tmp.get();
            // else
            // log.severe("no se encontro el objeto deespues de actualizar");

            // edit(objeto);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void delete(Entity item) {
        try {
            if (validateDelete(item)) {
                // elimina los metadatos
                repository.delete(item);
                postDelete(item);
                loadData();
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void delete() {
        try {
            if (validateDelete(objeto)) {
                // elimina los metadatos
                repository.delete(objeto);
                postDelete(objeto);
                loadData();
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
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
