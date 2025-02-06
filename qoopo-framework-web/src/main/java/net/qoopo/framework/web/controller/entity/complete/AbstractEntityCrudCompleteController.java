package net.qoopo.framework.web.controller.entity.complete;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ReorderEvent;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.QoopoFramework;
import net.qoopo.framework.exporter.Importer;
import net.qoopo.framework.filter.FilterJpaRepository;
import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.filter.core.condition.Value;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.jpa.core.interfaces.Auditable;
import net.qoopo.framework.jpa.core.interfaces.CoreMetadata;
import net.qoopo.framework.jpa.core.interfaces.Ordenable;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.repository.QoopoJpaRepositorySingleton;
import net.qoopo.framework.util.QLogger;
import net.qoopo.framework.util.QoopoUtil;
import net.qoopo.framework.web.components.viewoption.ViewOption;
import net.qoopo.framework.web.controller.AbstractCrudCompleteController;
import net.qoopo.framework.web.core.interfaces.AdminBeanProgressable;
import net.qoopo.framework.web.util.FacesUtils;

/**
 * Clase de esqueleto de los beans de administración
 *
 * @author alberto
 * @param <T>
 */

@Getter
@Setter
public abstract class AbstractEntityCrudCompleteController<T extends AbstractEntity>
        extends AbstractCrudCompleteController<T, T, Long>
        implements AdminBeanProgressable {

    public AbstractEntityCrudCompleteController(String entityClassName, Class<T> entityClass, Filter inicial,
            List<Condition> condicionesDisponibles,
            List<Field> campos,
            List<OpcionBase> opcionesGrupos) {
        super(entityClassName, entityClass);
        this.inicial = inicial;
        this.campos.addAll(campos);
        this.opcionesGrupos.addAll(opcionesGrupos);
        this.condicionesDisponibles.addAll(condicionesDisponibles);

        setFilterRepository(new FilterJpaRepository<>(QoopoFramework.get().getDataSourceName(), entityClass));
        log.info("adminabstractclass ->" + entityClassName);
    }

    public void procesarParametro() {
        try {
            String filterValueTmp = "";
            if (FacesUtils.getRequestParameter("filterValue") != null) {
                filterValueTmp = FacesUtils.getRequestParameter("filterValue");
            }
            String filterValue = filterValueTmp;
            if (FacesUtils.getRequestParameter("filter") != null) {
                filterName = FacesUtils.getRequestParameter("filter");
                if (condicionesDisponibles != null && !condicionesDisponibles.isEmpty()) {
                    condicionesDisponibles.stream()
                            .filter(c -> c.getName().equals(filterName))
                            .collect(Collectors.toList())
                            .forEach(c1 -> {
                                Condition c2 = c1.clonar();
                                if (filterValue != null && !filterValue.isEmpty()) {
                                    switch (c2.getField().getTipo()) {
                                        case Field.INTEGER:
                                            c2.setValue(new Value(Integer.valueOf(filterValue)));
                                            break;
                                        case Field.LONG:
                                            c2.setValue(new Value(Long.valueOf(filterValue)));
                                            break;
                                        case Field.BOLEANO:
                                            c2.setValue(new Value(Boolean.valueOf(filterValue)));
                                            break;
                                        case Field.NUMERICO:
                                            c2.setValue(new Value(new BigDecimal(filterValue)));
                                            break;
                                        case Field.FECHA:
                                            c2.setValue(new Value(
                                                    LocalDateTime.from(QoopoUtil.getSDF().parse(filterValue))));
                                            break;
                                        case Field.STRING:
                                        default:
                                            c2.setValue(new Value(filterValue));
                                            break;
                                    }
                                    c2.setName(filterName + " = " + filterValue);
                                    log.info("[+] cambiando valor del filtro " + filterName + " a " + filterValue);
                                }
                                // filter.limpiar();
                                filter.seleccionarCondicion(c2);
                            });
                }
            }

            // JpaParameters del tipo vista
            if (FacesUtils.getRequestParameter("view") != null) {
                viewOption.setValue(FacesUtils.getRequestParameter("view"));
            }

            if (viewOption.getValue() == ViewOption.FORM) {
                // carga un objeto con el id del JpaParameters
                if (FacesUtils.getRequestParameter("id") != null) {
                    Optional<T> tmp = repository.find(Long.valueOf(FacesUtils.getRequestParameter("id")));
                    if (tmp.isPresent()) {
                        log.info("Se encontro el id parametrer y se va a edit");
                        edit(tmp.get());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo generico que elimina un registro
     *
     * @param item
     */
    public void delete(T item) {
        try {
            if (validateDelete(item)) {
                // elimina los metadatos
                if (item instanceof Auditable) {
                    // solo agrega un metadato en caso que no exista uno
                    if (((Auditable) item).getMetadato() != null) {
                        // CoreMetadata metaDatos = (CoreMetadata)
                        // GenericBusiness.buscar(CoreMetadata.class,((Auditable)
                        // item).getMetadato().getId());
                        CoreMetadata metaDatos = ((Auditable) objeto).getMetadato();
                        // actualiza el item para que ya no apunte a los metadato
                        ((Auditable) item).setMetadato(null);
                        repository.save(item);
                        if (metaDatos != null) {
                            QoopoJpaRepositorySingleton.deleteAll(metaDatos.getAuditorias());
                            QoopoJpaRepositorySingleton.deleteAll(metaDatos.getActividades());
                            QoopoJpaRepositorySingleton.delete(metaDatos);
                        }
                    }
                }
                super.delete(item);
                // en caso de estar mostrando el objeto, debe regresar a la vista default
                viewOption.reset();
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue("common.error") + ": " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Metodo que elimina el registro abierto
     */
    public void delete() {
        try {
            if (validateDelete(objeto)) {
                // elimina los metadatos
                if (objeto instanceof Auditable) {
                    // solo agrega un metadato en caso que no exista uno
                    if (((Auditable) objeto).getMetadato() != null) {
                        // CoreMetadata metaDatos = (CoreMetadata)
                        // GenericBusiness.buscar(CoreMetadata.class,((Auditable)
                        // objeto).getMetadato().getId());
                        CoreMetadata metaDatos = ((Auditable) objeto).getMetadato();
                        // actualiza el item para que ya no apunte a los metadato
                        ((Auditable) objeto).setMetadato(null);
                        repository.save(objeto);
                        if (metaDatos != null) {
                            QoopoJpaRepositorySingleton.deleteAll(metaDatos.getAuditorias());
                            QoopoJpaRepositorySingleton.deleteAll(metaDatos.getActividades());
                            QoopoJpaRepositorySingleton.delete(metaDatos);
                        }
                    }
                }
                super.delete();
                if (nav.getActual() >= getTotal()) {
                    nav.setActual(getTotal() - 1);
                }
                seleccionar(nav.getActual());
                viewOption.reset();
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue("common.error") + ": " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void deleteSelected() {
        try {
            masivo = true;
            StringBuilder sb = new StringBuilder();
            boolean error = false;
            if (selectedData != null && !selectedData.isEmpty()) {
                for (T item : selectedData) {
                    try {
                        if (validateDelete(item)) {
                            repository.delete(item);
                            postDelete(item);
                        }
                    } catch (Exception e) {
                        // FacesUtils.addErrorMessage(e);
                        sb.append(e.getLocalizedMessage()).append("\n");
                        error = true;
                    }
                }
                loadData();
                if (error) {
                    FacesUtils.addErrorMessage(sb.toString());
                }
            } else {
                FacesUtils.addWarningMessage(languageProvider.getTextValue(1027));
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e);
        }
        masivo = false;
    }

    /**
     * Método que debe ser llamado para la edición de un registro, desde la
     * vista lista o la vista de ícono
     *
     * @param item
     */
    public void edit(T item) {
        super.edit(item);
        log.info("edit ->" + entityClassName + " -> " + item);

        long tInicio = System.currentTimeMillis();
        // camba la url para mostrar el id actual
        if (item != null && !masivo) {
            sessionBean.addUrlParam("id", String.valueOf(item.getId()));
            sessionBean.addUrlParam("view", "form");
        }

        if (!masivo)
            viewOption.setValue(ViewOption.FORM);

        if (chatter != null) {
            if (item instanceof Auditable) {
                // en caso que este objeto no tenga metadato creamos uno
                if (((Auditable) item).getMetadato() == null) {
                    ((Auditable) item).setMetadato(sessionBean.addEditedEvent(((Auditable) item).getMetadato()));
                }

                // actualizo la url en el metadato
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                        .getRequest();
                String url = request.getRequestURL().toString();
                // String uri = request.getRequestURI();
                ((Auditable) item).getMetadato().setObjetoUrl(url + "?id=" + item.getId());
                chatter.mostrar(((Auditable) item).getMetadato(), (Auditable) objeto);
            } else {
                chatter.mostrar(null, null);
            }
        } else {
            log.severe("EL BEAN DEL METADATOS ESTA NULO !!!");
        }
        log.info("[+] edit [" + QLogger.getTime(tInicio) + "]");
    }

    /**
     * por cada item carga valores propios de la inicializacion, como la empresa
     * a la que pertenece
     *
     * @param item
     */
    public void importarItem(T item, Importer importer) {

    }

    public void importar() {
        importar(importer, () -> data.iterator().next());
    }

    public void importar(Importer importer, Supplier<T> factory) {
        StringBuilder sb = new StringBuilder();
        boolean error = false;
        try {
            log.info("[+] Importando");
            if (factory == null) {
                log.severe("[!] No se proveyo un factory para importar");
                return;
            }
            importer.setInputStream(getInputArchivoImport());
            importer.importar();
            getInputArchivoImport().close();
            while (importer.hasItems()) {
                try {
                    importer.startItem();
                    T item = factory.get();
                    if (item != null) {
                        item.importar(importer);
                        importarItem(item, importer);
                        repository.save(item);
                        log.log(Level.INFO, "[+] Registro importado:{0}", item.toString());
                    } else {
                        log.severe("[!] El factory devolvio nulo");
                    }
                } catch (Exception ex) {
                    // FacesUtils.addErrorMessage(ex);
                    sb.append("Error: Line-> ").append(importer.getCurrent()).append(" ");
                    sb.append(ex.getLocalizedMessage()).append("\n");
                    // sb.append(QoopoUtil.print(ex)).append("\n");
                    error = true;
                    ex.printStackTrace();
                }
                importer.endItem();
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
        }
        loadData();
        if (error) {
            FacesUtils.addErrorMessage(sb.toString());
        }
    }

    public ViewOption getViewOption() {
        return viewOption;
    }

    /**
     * Metodo que es llamado desde la vista de columna cuando se realiza un
     * drag&drop. Sebe ser sobrecargado a conveniencia
     *
     * @param ddEvent
     */
    public void onItemColumnDrop(DragDropEvent ddEvent) {
        columnDragDrop.proccess(ddEvent);
    }

    public void onRowReorder(ReorderEvent event) {
        try {
            int i = 0;
            for (Object item : getData()) {
                if (item instanceof Ordenable) {
                    ((Ordenable) item).setOrder(i);
                }
                i++;
            }
            repository.saveAll(getData());
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e);
        }
        // FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Row Moved",
        // "From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        // FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    @Override
    public void onComplete() {
        FacesUtils.addInfoMessage(languageProvider.getTextValue(1938));
    }

    public void resetView() {
        sessionBean.removeUrlParam("id");
        viewOption.reset();
        sessionBean.addUrlParam("view", viewOption.getStringValue());
    }
}
