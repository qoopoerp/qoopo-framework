package net.qoopo.framework.web.core.jpa.nolazy;

import java.util.List;

import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.jpa.filter.Filter;
import net.qoopo.framework.jpa.filter.condicion.Campo;
import net.qoopo.framework.jpa.filter.condicion.Condicion;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.web.core.jpa.AbstractAdminController;

/**
 * Clase de esqueleto de los beans de administración que no manejan los datos
 * con LazyDataTable
 *
 * @author alberto
 * @param <T>
 */
public abstract class AbstractNoLazyAdminController<T extends AbstractEntity> extends AbstractAdminController<T> {

    public AbstractNoLazyAdminController(String entityClassName, Class<T> entityClass, Filter inicial,
            List<Condicion> condicionesDisponibles,
            List<Campo> campos, List<OpcionBase> opcionesGrupos) {
        super(entityClassName, entityClass, inicial, condicionesDisponibles, campos, opcionesGrupos);
    }

    private List<T> lista;

    /**
     * Método que debe ser llamado para la edición de un registro, desde la
     * vista lista o la vista de ícono
     *
     * @param item
     */
    @Override
    public void edit(T item) {
        super.edit(item);
        try {
            nav.setActual(lista.indexOf(item) + 1);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Método interno usado por los botones de navegación (anterior, siguiente,
     * etc)
     *
     * @param indice
     */
    @Override
    public void seleccionar(int indice) {
        if (!lista.isEmpty()) {
            edit(lista.get(indice - 1));
        }
    }

    /**
     * Recorre al último objeto de la lista
     *
     * @return
     */
    @Override
    public int getTotal() {
        if (getLista() != null) {
            return getLista().size();
        } else {
            return 0;
        }
    }

    /**
     * Devuelve la lista de los objetos
     *
     * @return
     */
    public List<T> getLista() {
        return lista;
    }

    /**
     * Recibe la lista de los objetos
     *
     * @param lista
     */
    public void setLista(List<T> lista) {
        this.lista = lista;
        super.loadData(lista);
    }

    /**
     * Realiza la carga de los registros utilizando los filtors, en caso de querer
     * cargar la data usando otro mecanismo se debe sobreescribir en las
     * implementaciones
     */
    public void loadData() {
        setLista(filterRepository.filtrar(filter.getFiltro()));
        listaSeleccionados.clear();
        // QoopoUtil.launchErrorTrace();
    }

}
