package net.qoopo.qoopoframework.web.core.dto.lazy;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import net.qoopo.qoopoframework.core.business.Filtros;
import net.qoopo.qoopoframework.core.db.core.base.EntidadBase;
import net.qoopo.qoopoframework.core.db.core.base.dtos.DtoBase;
import net.qoopo.qoopoframework.core.db.core.base.dtos.base.OpcionBase;
import net.qoopo.qoopoframework.core.db.filtro.Filtro;
import net.qoopo.qoopoframework.core.db.filtro.condicion.Campo;
import net.qoopo.qoopoframework.core.db.filtro.condicion.Condicion;
import net.qoopo.qoopoframework.web.components.viewoption.ViewOption;
import net.qoopo.qoopoframework.web.core.dto.AdminDtoAbstractClass;
import net.qoopo.qoopoframework.web.util.FacesUtils;

/**
 * Clase de esqueleto de los beans de administración con modo Lazy
 * (LazyDataTable)
 *
 * @author alberto
 * @param <T>
 */
public abstract class AdminLazyDtoAbstractClass<S extends EntidadBase, T extends DtoBase>
        extends AdminDtoAbstractClass<S, T> {

    public AdminLazyDtoAbstractClass(String entityClassName, Class<S> entityClass, Filtro inicial,
            List<Condicion> condicionesDisponibles,
            List<Campo> campos, List<OpcionBase> opcionesGrupos) {
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
                                String posterior = filter.getFiltro().getPosterior();
                                if (sortBy != null && !sortBy.isEmpty()) {
                                    posterior = " order by ";
                                    for (SortMeta meta : sortBy.values()) {
                                        posterior += " o." + getSortField(meta.getField()) + " "
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
                                // filter.getFiltro().agregarCondicion(Condicion.build(new
                                // Campo(meta.getFilterField(), "o." + meta.getFilterField()), Funcion.CONTIENE,
                                // new Valor("val", "%" + (String) meta.getFilterValue() + "%")),
                                // Condicion.AND);
                                // }
                                // }
                                filter.getFiltro().setPosterior(posterior);
                                if (listaSeleccionados != null)
                                    listaSeleccionados.clear();
                                return Filtros.filtrar(filter.getFiltro(), first, pageSize);
                            }

                            @Override
                            public int count(Map<String, FilterMeta> map) {
                                return Filtros.filtrarCount(filter.getFiltro()).intValue();
                            }
                        };
                    }
                    lista.setRowCount(Filtros.filtrarCount(filter.getFiltro()).intValue());
                    setData(lista.getWrappedData());// la data para poder exportar la pagina actual
                    break;
                case ViewOption.GRID:
                case ViewOption.GRAPH:
                case ViewOption.FORM:
                case ViewOption.CALENDAR:
                case ViewOption.TIMELINE:
                    super.loadData(Filtros.filtrar(filter.getFiltro()));
                    break;
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
            ex.printStackTrace();
            Logger.getLogger(AdminLazyDtoAbstractClass.class.getName()).log(Level.SEVERE, null, ex);
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
        lista.setRowIndex(indice - 1); // los indices van desde 0 a largo -1
        try {
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

    /**
     * Devuelve la lista de los objetos
     *
     * @return
     */
    public LazyDataModel<T> getLista() {
        return lista;
    }

    /**
     * Recibe la lista de los objetos
     *
     * @param lista
     */
    public void setLista(LazyDataModel<T> lista) {
        this.lista = lista;
    }

}
