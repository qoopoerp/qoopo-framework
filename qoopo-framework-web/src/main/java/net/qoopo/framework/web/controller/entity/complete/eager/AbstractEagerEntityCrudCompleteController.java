package net.qoopo.framework.web.controller.entity.complete.eager;

import java.util.List;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.web.controller.entity.complete.AbstractEntityCrudCompleteController;

/**
 * Clase de esqueleto de los beans de administración que no manejan los datos
 * con LazyDataTable
 *
 * @author alberto
 * @param <T>
 */
@Getter
@Setter
public abstract class AbstractEagerEntityCrudCompleteController<T extends AbstractEntity>
        extends AbstractEntityCrudCompleteController<T> {

    private static Logger log = Logger.getLogger("abstract-eager-entity-crud-complete-controller");

    public AbstractEagerEntityCrudCompleteController(String entityClassName, Class<T> entityClass, Filter inicial,
            List<Condition> condicionesDisponibles,
            List<Field> campos, List<OpcionBase> opcionesGrupos) {
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
        setLista(filterRepository.apply(filter.getFiltro()));
        selectedData.clear();
    }

}
