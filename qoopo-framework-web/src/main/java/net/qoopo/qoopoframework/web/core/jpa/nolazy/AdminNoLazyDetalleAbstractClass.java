package net.qoopo.qoopoframework.web.core.jpa.nolazy;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.qoopoframework.jpa.core.EntidadBase;
import net.qoopo.qoopoframework.jpa.filter.Filter;
import net.qoopo.qoopoframework.jpa.filter.condicion.Campo;
import net.qoopo.qoopoframework.jpa.filter.condicion.Condicion;
import net.qoopo.qoopoframework.models.OpcionBase;
import net.qoopo.qoopoframework.web.util.FacesUtils;
import net.qoopo.util.db.repository.CrudRepository;

/**
 * Clase de esqueleto de los beans de administración
 *
 * @author alberto
 * @param <S>
 * @param <T>
 */
public abstract class AdminNoLazyDetalleAbstractClass<S extends EntidadBase, T> extends AdminNoLazyAbstractClass<S> {

    public AdminNoLazyDetalleAbstractClass(String entityClassName, Class<S> entityClass, Filter inicial,
            List<Condicion> condicionesDisponibles, List<Campo> campos, List<OpcionBase> opcionesGrupos) {
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
    public void edit(S item) {
        super.edit(item); // To change body of generated methods, choose Tools | Templates.
        listaEliminar.clear();
    }

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
