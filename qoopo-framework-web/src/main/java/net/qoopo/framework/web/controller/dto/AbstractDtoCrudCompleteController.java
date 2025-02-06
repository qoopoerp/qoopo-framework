package net.qoopo.framework.web.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.timeline.TimelineEvent;

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
import net.qoopo.framework.jpa.core.dtos.DtoBase;
import net.qoopo.framework.jpa.core.interfaces.Agrupable;
import net.qoopo.framework.jpa.core.interfaces.Auditable;
import net.qoopo.framework.jpa.core.interfaces.CoreMetadata;
import net.qoopo.framework.jpa.core.interfaces.Eventable;
import net.qoopo.framework.jpa.core.interfaces.Graficable;
import net.qoopo.framework.jpa.core.interfaces.Ordenable;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.repository.QoopoJpaRepositorySingleton;
import net.qoopo.framework.util.QLogger;
import net.qoopo.framework.util.QoopoUtil;
import net.qoopo.framework.web.components.kanban.KanbanColumn;
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
public abstract class AbstractDtoCrudCompleteController<S extends AbstractEntity, T extends DtoBase>
        extends AbstractCrudCompleteController<S, T, Long>
        implements AdminBeanProgressable {

    protected boolean entitiesLoaded = false;

    /**
     * Transforma el campo del dto al campo de la entidad para agregar en el sortby
     * (Necesario para los LazyModel)
     * del sql
     */
    public String getSortField(String field) {
        return field;
    }

    public AbstractDtoCrudCompleteController(String entityClassName, Class<T> entityClass, Filter inicial,
            List<Condition> condicionesDisponibles,
            List<Field> campos,
            List<OpcionBase> opcionesGrupos) {
        super(entityClassName, entityClass);
        this.inicial = inicial;
        this.campos.addAll(campos);
        this.opcionesGrupos.addAll(opcionesGrupos);
        this.condicionesDisponibles.addAll(condicionesDisponibles);
        setFilterRepository(new FilterJpaRepository<>(QoopoFramework.get().getDataSourceName(), entityClass));
        log.info("AdminDtoabstractClass ->" + entityClassName);
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

            // carga un objeto con el id del JpaParameters
            if (FacesUtils.getRequestParameter("id") != null) {
                Optional<S> tmp = repository.find(Long.valueOf(FacesUtils.getRequestParameter("id")));
                if (tmp.isPresent()) {
                    log.info("Se encontro el id parametrer y se va a edit");
                    editItem(tmp.get());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obliga a cargar las entidades de los DTO
     */
    protected void loadEntities() {
        if (!entitiesLoaded) {
            long tInicio = System.currentTimeMillis();
            data.forEach(c -> c.setEntity(findEntity(c)));
            log.warning("[!] Loading entities... " + QLogger.getTime(tInicio));
            entitiesLoaded = true;
        }
    }

    protected void loadData(Iterable<T> data) {
        entitiesLoaded = false;
        super.loadData(data);
    }

    /**
     * PREPARA LAS COLUMNAS PARA LA VISTA KANBAN
     * Estas columnas no permite hacer drag and drop.
     * En caso de requerir hay que implementarlo en el bean. Vease
     * OportunidadesBean.java
     * 
     * @param data
     */
    @Override
    public void loadKanban(Iterable<T> data) {
        // vista kanban
        this.columnas.clear();
        if (viewOption.getValue() == ViewOption.GRID) {
            // log.info("loading kanban...");
            List<String> tmpColumnsIds = new ArrayList<>();
            if (filter != null && filter.getCampoGrupo() != null) {
                // log.log(Level.INFO, "group is setted {0}",
                // filter.getCampoGrupo().getTexto());
                switch (filter.getCampoGrupo().getCodigo()) {
                    case OpcionBase.SIN_GRUPO_CODE:
                        // log.info("opcion sin grupo");
                        // en blanco para que no agrupe
                        break;
                    default:// todos los campos, sin permitir hacer drag and drop
                        if (data != null /* && !data.isEmpty() */) {
                            for (T o : data) {
                                if (o instanceof Graficable) {
                                    o.setEntity(findEntity(o));
                                    for (String columnValue : ((Graficable) o).getGrupo(filter.getCampoGrupo())) {
                                        KanbanColumn column = new KanbanColumn<String, T>(columnValue, columnValue,
                                                null,
                                                false);
                                        if (!tmpColumnsIds.contains(columnValue)) {
                                            columnas.add(column);
                                            tmpColumnsIds.add(columnValue);
                                        }
                                        for (KanbanColumn e : columnas) {
                                            if (((String) e.getObjeto()).equals(columnValue)) {
                                                e.getItems().add(o);
                                            }
                                        }
                                    }
                                } else {
                                    log.severe("[!] No es de tipo graficable");
                                }
                            }
                        }
                        Collections.sort(columnas,
                                (KanbanColumn t, KanbanColumn t1) -> t.getNombre().compareTo(t1.getNombre()));
                        break;
                }
            } else {
                // log.info("not group setted");
            }
        }
    }

    /**
     * Carga los eventos que se encuentren en la lista
     *
     * @param lista
     */
    @Override
    protected void loadEvents(Iterable<T> lista) {
        try {
            eventModel.clear();
            if (viewOption.getValue() == ViewOption.CALENDAR && lista != null) {
                lista.forEach(t -> {
                    if (t instanceof Eventable) {
                        try {
                            t.setEntity(findEntity(t));
                            Eventable ob = (Eventable) t;
                            if (ob.getFechaInicio() != null) {
                                DefaultScheduleEvent tmp = DefaultScheduleEvent.builder().title(ob.getEventoNombre())
                                        .data(ob)
                                        .startDate(ob.getFechaInicio())
                                        .endDate(ob.getFechaFin() != null ? ob.getFechaFin() : ob.getFechaInicio())
                                        .styleClass(ob.getStyleEvent())
                                        .build();
                                tmp.setAllDay(ob.isAllDay());
                                eventModel.addEvent(tmp);
                            }
                        } catch (Exception e) {
                            log.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            // log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Carga la estructura arbol con los datos provistos
     *
     * @param lista
     */
    @Override
    protected void loadTree(Iterable<T> lista) {
        try {
            if (viewOption.getValue() == ViewOption.LIST) {
                // tree.setDatos(lista);
                // tree.setBean(this); //<pendiente>
                // tree.recargar();
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Carga la vista de grafico
     *
     * @param lista
     */
    @Override
    protected void loadGraph(Iterable<T> lista) {
        try {
            this.graph.clear();
            if (viewOption.getValue() == ViewOption.GRAPH) {
                // para este caso debemos recargar las entities
                Long tInicio = System.currentTimeMillis();
                lista.forEach(c -> c.setEntity(findEntity(c)));
                log.info("--> Entities cargadas para graph: " + QLogger.getTime(tInicio));
                this.graph.setDatos(lista);
                this.graph.recargar();
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Carga los eventos que se encuentren en la lista para el timeline
     *
     * @param lista
     */
    @Override
    protected TimelineEvent createTimeLineEvent(Eventable ob, String grupo) {
        TimelineEvent event = TimelineEvent.builder()
                .data(ob)
                // .data(ob.getEventoNombre())
                .title(ob.getEventoNombre())
                .startDate(ob.getFechaInicio())
                .endDate(ob.getFechaFin() != null ? ob.getFechaFin()
                        : ob.getFechaInicio())
                .editable(true)
                .group(grupo)
                .styleClass(ob.getStyleEvent())
                .build();
        return event;
    }

    @Override
    protected void loadTimeLine(Iterable<T> lista) {
        try {
            timeLineModel.clear();
            if (viewOption.getValue() == ViewOption.TIMELINE && lista != null) {
                lista.forEach(t -> {
                    if (t instanceof Eventable) {
                        try {
                            Eventable ob = (Eventable) t;
                            t.setEntity(findEntity(t));
                            if (ob.getFechaInicio() != null) {
                                String grupo = ob.getEventoNombre();
                                if (t instanceof Agrupable && filter != null && filter.getCampoGrupo() != null
                                        && !filter.getCampoGrupo().equals(OpcionBase.SIN_GRUPO)) {
                                    for (String columnValue : ((Agrupable) t).getGrupo(filter.getCampoGrupo())) {
                                        grupo = columnValue;
                                        timeLineModel.add(createTimeLineEvent(ob, grupo));
                                    }
                                } else {
                                    timeLineModel.add(createTimeLineEvent(ob, grupo));
                                }
                            }
                        } catch (Exception e) {
                            log.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                });

                // set initial start / end dates for the axis of the timeline
                // timeLineStart = LocalDate.now().plusDays(-3).atStartOfDay();
                // timeLineEnd = LocalDate.now().plusDays(4).atStartOfDay();
            }
        } catch (Exception e) {
            // log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Metodo generico que elimina un registro
     *
     * @param item
     */
    public void delete(T itemToDelete) {
        try {
            S item = findEntity(itemToDelete);
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

    /**
     * Metodo generico que guarda un nuevo objeto
     */
    public void save() {
        try {
            if (objeto instanceof Auditable) {
                ((Auditable) objeto).setMetadato(sessionBean.addCreatedEvent(((Auditable) objeto).getMetadato()));
                chatter.saveProperties();
            }
            super.save();
            editItem(objeto);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Metodo generico que actualiza un objeto
     */
    public void update() {
        // editando = false;
        try {
            if (objeto instanceof Auditable) {
                // solo agrega un metadato en caso que no exista uno
                if (((Auditable) objeto).getMetadato() == null) {
                    ((Auditable) objeto).setMetadato(sessionBean.addEditedEvent(((Auditable) objeto).getMetadato()));
                }
                chatter.saveProperties(false); // ya no guarda los metadatos pues se guardan en cascada con el edit
                                               // siguiente a esta linea
            }
            super.save();
            // Optional<S> tmp = repository.find(objeto.getId());
            // if (tmp.isPresent())
            // objeto = tmp.get();
            // else
            // log.severe("no se encontro el objeto deespues de actualizar");
            editItem(objeto);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void deleteSelected() {
        try {
            StringBuilder sb = new StringBuilder();
            boolean error = false;
            masivo = true;
            if (selectedData != null && !selectedData.isEmpty()) {
                for (T item : selectedData) {
                    try {
                        if (validateDelete(findEntity(item))) {
                            repository.delete(findEntity(item));
                            postDelete(findEntity(item));
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

    public void edit(T item) {
        log.info("dto edit ->" + entityClassName);
        long tInicio = System.currentTimeMillis();
        if (item instanceof DtoBase) {
            try {
                editItem(findEntity(item));
                ((DtoBase) item).setEntity(findEntity(item));
                editItem((S) ((DtoBase) item).getEntity());
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        } else {
            try {
                editItem((S) (AbstractEntity) item);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        log.info("[+] edit [" + QLogger.getTime(tInicio) + "]");
    }

    /**
     * Método que debe ser llamado para la edición de un registro, desde la
     * vista lista o la vista de ícono
     *
     * @param item
     */
    public void editItem(S item) {
        super.edit(item);

        log.info("editItem ->" + entityClassName + " -> " + item);
        // cambio la url para mostrar el id actual
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
    }

    public void exportar() {
        loadEntities();
        super.exportar();
    }

    /**
     * por cada item carga valores propios de la inicializacion, como la empresa
     * a la que pertenece
     *
     * @param item
     */
    public void importarItem(S item, Importer importer) {

    }

    public void importar() {
        importar(importer, () -> findEntity(data.iterator().next()));
    }

    public void importar(Importer importer, Supplier<S> factory) {
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
                    S item = factory.get();
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
            for (T item : getData()) {
                if (item instanceof Ordenable) {
                    ((Ordenable) item).setOrder(i);
                    repository.save(findEntity(item));
                }
                i++;
            }
            // repository.saveAll(getData());
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e);
        }
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

    protected abstract S findEntity(T dto);

}
