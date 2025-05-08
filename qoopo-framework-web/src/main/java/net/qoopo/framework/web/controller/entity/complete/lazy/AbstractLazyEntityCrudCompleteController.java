package net.qoopo.framework.web.controller.entity.complete.lazy;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.web.components.viewoption.ViewOption;
import net.qoopo.framework.web.controller.entity.complete.AbstractEntityCrudCompleteController;
import net.qoopo.framework.web.util.FacesUtils;

/**
 * Clase de esqueleto de los beans de administración con modo Lazy
 * (LazyDataTable)
 *
 * @author alberto
 * @param <T>
 */
@Getter
@Setter
public abstract class AbstractLazyEntityCrudCompleteController<T extends AbstractEntity>
        extends AbstractEntityCrudCompleteController<T> {

    private static Logger log = Logger.getLogger("abstract-lazy-entity-crud-complete-controller");

    public AbstractLazyEntityCrudCompleteController(String entityClassName, Class<T> entityClass, Filter inicial,
            List<Condition> condicionesDisponibles,
            List<Field> campos, List<OpcionBase> opcionesGrupos) {
        super(entityClassName, entityClass, inicial, condicionesDisponibles, campos, opcionesGrupos);
    }

    protected LazyDataModel<T> lista = null;

    protected abstract T buscar(String rowKey);

    @Override
    public void loadData() {
        try {
            switch (viewOption.getValue()) {
                case ViewOption.LIST:
                    // instancia de la lista, define el comportamiento
                    if (lista == null) {
                        lista = new LazyDataModel<T>() {

                            @Override
                            public String getRowKey(T object) {
                                return object.getId().toString();
                            }

                            @Override
                            public T getRowData(String rowKey) {
                                return buscar(rowKey);
                            }

                            @Override
                            public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy,
                                    Map<String, FilterMeta> filterBy) {
                                String posterior = filter.getFiltro().getNext();
                                // Ordenamiento
                                if (sortBy != null && !sortBy.isEmpty()) {
                                    posterior = " order by ";
                                    for (SortMeta meta : sortBy.values()) {
                                        posterior += " o." + meta.getField() + " "
                                                + (SortOrder.ASCENDING.equals(meta.getOrder()) ? " asc " : " desc ");
                                    }
                                    // como cambio el orden enfuncion de las columnas y es un order by completo,
                                    // quito el dle filtro
                                    filter.getFiltro().setOrderDirection("");
                                }
                                // //filtro incluido en las cabeceras de las tablas
                                // if (filterBy != null) {
                                // filter.getFiltro().setCondicion(null);
                                // for (FilterMeta meta : filterBy.values()) {
                                // filter.getFiltro().appendCondition(Condition.build(new
                                // Field(meta.getFilterField(), "o." + meta.getFilterField()), Funcion.CONTIENE,
                                // new Valor("val", "%" + (String) meta.getFilterValue() + "%")),
                                // Condition.AND);
                                // }
                                // }
                                filter.getFiltro().setNext(posterior);
                                if (selectedData != null)
                                    selectedData.clear();
                                return filterRepository.apply(filter.getFiltro(), first, pageSize);
                            }

                            @Override
                            public int count(Map<String, FilterMeta> map) {
                                Long value = filterRepository.applyCount(filter.getFiltro());
                                return value != null ? value.intValue() : 0;
                            }
                        };
                    }
                    Long value = filterRepository.applyCount(filter.getFiltro());
                    lista.setRowCount(value != null ? value.intValue() : 0);
                    log.info("row count-> " + lista.getRowCount());
                    setData(lista.getWrappedData());// la data para poder exportar la pagina actual
                    // super.loadData(lista); //carga la data para las otras vistas
                    break;
                case ViewOption.GRID:
                case ViewOption.GRAPH:
                case ViewOption.FORM:
                case ViewOption.CALENDAR:
                case ViewOption.TIMELINE:
                    super.loadData(filterRepository.apply(filter.getFiltro()));
                    break;
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
            Logger.getLogger(AbstractLazyEntityCrudCompleteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            nav.setActual(lista.getWrappedData().indexOf(item) + 1);
        } catch (Exception e) {
        }
    }

    /**
     * Método interno usado por los botones de navegación (anterior, siguiente,`
     * etc)
     *
     * @param indice
     */
    @Override
    public void seleccionar(int indice) {
        try {
            lista.setRowIndex(indice - 1); // los indices van desde 0 a largo -1
            edit(lista.getRowData());
        } catch (Exception e) {
            //
        }
    }

    @Override
    public int getTotal() {
        if (lista != null)
            return Math.min(lista.getPageSize(), lista.getRowCount());
        else
            return 0;
    }

}
