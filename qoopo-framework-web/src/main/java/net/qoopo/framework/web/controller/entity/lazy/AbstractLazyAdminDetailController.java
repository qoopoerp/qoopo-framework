package net.qoopo.framework.web.controller.entity.lazy;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.data.repository.CrudRepository;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.web.util.FacesUtils;

/**
 * Clase de esqueleto de los beans de administración
 *
 * @author alberto
 * @param <S>
 * @param <T>
 */
public abstract class AbstractLazyAdminDetailController<S extends AbstractEntity, T> extends AbstractLazyAdminController<S> {

    public AbstractLazyAdminDetailController(String entityClassName, Class<S> entityClass, Filter inicial,
            List<Condition> condicionesDisponibles,
            List<Field> campos, List<OpcionBase> opcionesGrupos) {
        super(entityClassName, entityClass, inicial, condicionesDisponibles, campos, opcionesGrupos);
    }

    protected CrudRepository<T, Long> repositoryDetalle;
    protected T itemDetalle;
    protected boolean editandoDetalle;
    protected List<T> listaEliminar = new ArrayList<>();

    public abstract void nuevoDetalle();

    public abstract void agregarDetalle();

    public abstract void eliminarDetalle(T item);

    @Override
    public void update() {
        try {
            for (T det : listaEliminar) {
                repositoryDetalle.delete(det);
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue(20) + e.getMessage());
        }
        super.update();
    }

    @Override
    public void edit(S item) {
        super.edit(item); // To change body of generated methods, choose Tools | Templates.
        listaEliminar.clear();
    }

    public T getItemDetalle() {
        return itemDetalle;
    }

    public void setItemDetalle(T itemDetalle) {
        this.itemDetalle = itemDetalle;
    }

    public boolean isEditandoDetalle() {
        return editandoDetalle;
    }

    public void setEditandoDetalle(boolean editandoDetalle) {
        this.editandoDetalle = editandoDetalle;
    }

    public List<T> getListaEliminar() {
        return listaEliminar;
    }

    public void setListaEliminar(List<T> listaEliminar) {
        this.listaEliminar = listaEliminar;
    }

}
