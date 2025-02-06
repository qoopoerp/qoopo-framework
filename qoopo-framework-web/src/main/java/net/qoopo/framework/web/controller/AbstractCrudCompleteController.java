package net.qoopo.framework.web.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.schedule.ScheduleEntryMoveEvent;
import org.primefaces.event.schedule.ScheduleEntryResizeEvent;
import org.primefaces.event.timeline.TimelineModificationEvent;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.jpa.core.interfaces.Agrupable;
import net.qoopo.framework.jpa.core.interfaces.Archivable;
import net.qoopo.framework.jpa.core.interfaces.Auditable;
import net.qoopo.framework.jpa.core.interfaces.Eventable;
import net.qoopo.framework.jpa.core.interfaces.Graficable;
import net.qoopo.framework.lang.LanguageProvider;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.multitenant.TenantProvider;
import net.qoopo.framework.reports.Reporte;
import net.qoopo.framework.util.QLogger;
import net.qoopo.framework.web.AppSessionBeanInterface;
import net.qoopo.framework.web.ImagenesBean;
import net.qoopo.framework.web.SecurityContextBean;
import net.qoopo.framework.web.components.graph.GraphController;
import net.qoopo.framework.web.components.kanban.ColumnDragDrop;
import net.qoopo.framework.web.components.kanban.KanbanColumn;
import net.qoopo.framework.web.components.tree.TreeController;
import net.qoopo.framework.web.components.viewoption.ViewOption;
import net.qoopo.framework.web.util.FacesUtils;
import net.qoopo.framework.web.vistas.ChatterInterface;
import net.qoopo.framework.web.vistas.ReporteBean;

/**
 * Controlador web para JSF que gestiona el proceso CRUD de entidades que
 * heredan de AbstractEntity
 */
@Getter
@Setter
public abstract class AbstractCrudCompleteController<Entity extends AbstractEntity, EntityData extends AbstractEntity, EntityID>
        extends AbstractCrudFilteredController<Entity, EntityData, EntityID> {

    @Inject
    protected SecurityContextBean securityBean;

    @Inject
    protected LanguageProvider languageProvider;

    @Inject
    protected ReporteBean reporteBean;

    @Inject
    protected ImagenesBean imagenesBean;

    @Inject
    protected AppSessionBeanInterface sessionBean;

    @Inject
    protected TenantProvider tenantProvider;

    protected boolean canArchive = true;
    protected ChatterInterface chatter;
    protected ScheduleEvent<EntityData> event = new DefaultScheduleEvent<>();
    protected TimelineModel<EntityData, Object> timeLineModel = new TimelineModel<>();
    protected TimelineEvent<EntityData> eventTimeLine;
    protected LocalDateTime timeLineStart;
    protected LocalDateTime timeLineEnd;
    protected transient Reporte reporte;
    protected GraphController<EntityData> graph = null;
    protected TreeController tree = null;
    protected int count;
    protected Integer progress;
    protected String progressStatus;

    // datos que se muestran actualmente
    protected List<KanbanColumn> columnas = new ArrayList<>(); // para la vista de kanban
    protected ColumnDragDrop columnDragDrop = new ColumnDragDrop();

    protected ScheduleModel eventModel = new DefaultScheduleModel();

    protected final Class<EntityData> entityClass;

    /**
     * Accion que se ejecuta para la actualizacion del valor de progress
     *
     * Ejemplo de uso new Thread(() -> {
     * InventarioQuants.calculateQuants(empresa, accionUpdateProgress);
     * }).start();
     */
    protected Accion accionUpdateProgress = new Accion() {
        @Override
        public Object ejecutar(Object... parameters) {
            setProgress((Integer) parameters[0]);
            if (parameters.length > 1) {
                setProgressStatus((String) parameters[1]);
            }
            return (Integer) parameters[0];
        }
    };

    /**
     * Accion que se ejecuta en los filtros, predeterminado cargar lista
     */
    // protected Accion accion=new Accion(){@Override public Object
    // ejecutar(Object...parameters){loadData();if(sessionBean!=null&&viewOption!=null){sessionBean.addUrlParam("view",viewOption.getStringValue());}return
    // null;}};

    protected final ViewOption viewOption = new ViewOption(accion);

    protected abstract void initChatter();

    public AbstractCrudCompleteController(String entityClassName, Class<EntityData> entityClass) {
        super(entityClassName);
        this.entityClass = entityClass;
    }

    @PostConstruct
    public void postConstruct() {
        graph = new GraphController<EntityData>(languageProvider);
        tree = new TreeController();
        initChatter();
    }

    protected void loadData(Iterable<EntityData> data) {
        if (selectedData != null)
            selectedData.clear();
        long tInicio = System.currentTimeMillis();
        setData(data);
        loadEvents(data);
        loadGraph(data);
        loadTree(data);
        loadKanban(data);
        loadTimeLine(data);
        sessionBean.addUrlParam("view", viewOption.getStringValue());
        log.info("[+] load data [" + QLogger.getTimeFormater(System.currentTimeMillis() - tInicio));
    }

    /**
     * PREPARA LAS COLUMNAS PARA LA VISTA KANBAN
     * Estas columnas no permite hacer drag and drop.
     * En caso de requerir hay que implementarlo en el bean. Vease
     * OportunidadesBean.java
     * 
     * @param data
     */
    protected void loadKanban(Iterable<EntityData> data) {
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
                            for (EntityData o : data) {
                                if (o instanceof Graficable) {
                                    for (String columnValue : ((Graficable) o).getGrupo(filter.getCampoGrupo())) {
                                        KanbanColumn column = new KanbanColumn<String, EntityData>(columnValue,
                                                columnValue,
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
    protected void loadEvents(Iterable<EntityData> lista) {
        try {
            eventModel.clear();
            if (viewOption.getValue() == ViewOption.CALENDAR && lista != null) {
                lista.forEach(t -> {
                    if (t instanceof Eventable) {
                        try {
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
    protected void loadTree(Iterable<EntityData> lista) {
        try {
            if (viewOption.getValue() == ViewOption.LIST) {
                // tree.setDatos(lista);
                // tree.setBean(this);
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
    protected void loadGraph(Iterable<EntityData> lista) {
        try {
            this.graph.clear();
            if (viewOption.getValue() == ViewOption.GRAPH) {
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

    protected void loadTimeLine(Iterable<EntityData> lista) {
        try {
            timeLineModel.clear();
            if (viewOption.getValue() == ViewOption.TIMELINE && lista != null) {
                log.info("timeline - loading");
                lista.forEach(t -> {
                    if (t instanceof Eventable) {
                        try {
                            Eventable ob = (Eventable) t;
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
                // timeLineStart = LocalDate.now().plusDays(-3).atStartOfDay();
                // timeLineEnd = LocalDate.now().plusDays(4).atStartOfDay();
            }
        } catch (Exception e) {
            // log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void clonar() {
        try {
            Entity temp = (Entity) objeto.clonar();
            if (objeto instanceof Auditable) {
                if (((Auditable) temp).getMetadato() == null) {
                    ((Auditable) temp).setMetadato(sessionBean.addEvent(((Auditable) temp).getMetadato(),
                            "Clonado de " + objeto.toString()));
                }
                chatter.saveProperties();
            }
            nuevo();
            this.objeto = temp;
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public boolean validateArchive(Entity item) {
        return true;
    }

    /**
     * Metodo que archiva el registro abierto
     */
    public void archive() {
        try {
            if (objeto instanceof Archivable) {
                if (validateArchive(objeto)) {
                    ((Archivable) objeto).setArchived(true);
                    if (objeto instanceof Auditable) {
                        // solo agrega un metadato en caso que no exista uno
                        if (((Auditable) objeto).getMetadato() == null) {
                            ((Auditable) objeto).setMetadato(sessionBean.addEvent(((Auditable) objeto).getMetadato(),
                                    "Archivado"));
                        }
                        chatter.saveProperties();
                    }
                    update();
                }
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue("common.error") + ": " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Metodo que des-archiva el registro abierto
     */
    public void unarchive() {
        try {
            if (objeto instanceof Archivable) {
                ((Archivable) objeto).setArchived(false);
                if (objeto instanceof Auditable) {
                    // solo agrega un metadato en caso que no exista uno
                    if (((Auditable) objeto).getMetadato() == null) {
                        ((Auditable) objeto).setMetadato(sessionBean.addEvent(((Auditable) objeto).getMetadato(),
                                "Desarchivado"));
                    }
                    chatter.saveProperties();
                }
                update();
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue("common.error") + ": " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Método que debe ser llamado cuando se presione el botón "Crear" de las
     * plantillas
     */
    public void nuevo() {
        log.info("call new -> " + entityClassName);
        super.nuevo();
        sessionBean.removeUrlParam("id");
        viewOption.setValue(ViewOption.FORM);
        sessionBean.addUrlParam("view", "form");
        if (objeto instanceof Auditable) {
            chatter.mostrar(((Auditable) objeto).getMetadato(), (Auditable) objeto);
        } else {
            chatter.mostrar(null, null);
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
            edit(objeto);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Metodo generico que actualiza un objeto
     */
    public void update() {
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
            // Optional<T> tmp = repository.find(objeto.getId());
            // if (tmp.isPresent())
            // objeto = tmp.get();
            // else
            // log.severe("no se encontro el objeto deespues de actualizar");

            edit(objeto);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void addEvent(ActionEvent actionEvent) {
        if (event.getId() == null) {
            eventModel.addEvent(event);
        } else {
            eventModel.updateEvent(event);
        }
        event = new DefaultScheduleEvent<>();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        DefaultScheduleEvent _event = DefaultScheduleEvent.builder()
                .title("")
                .startDate((LocalDateTime) selectEvent.getObject())
                .endDate((LocalDateTime) selectEvent.getObject())
                .build();
        event = _event;
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        //
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        //
    }

    public void archivoSubido(FileUploadEvent event) {
        try {
            this.inputArchivoImport = event.getFile().getInputStream();
            FacesUtils.addInfoMessage(event.getFile().getFileName() + "  fue subido.");
        } catch (IOException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            FacesUtils.addErrorMessage("Error al cargar archivo.");
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        try {
            edit(((Entity) ((ScheduleEvent) selectEvent.getObject()).getData()));
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void onTimeLineSelect(TimelineSelectEvent e) {
        try {
            edit((Entity) e.getTimelineEvent().getData());
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
        }
    }

    public void onTimeLineEdit(TimelineModificationEvent e) {
        try {
            edit((Entity) e.getTimelineEvent().getData());
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
        }
    }

    public void onTimeLineDelete(TimelineModificationEvent e) {
        try {
            edit((Entity) e.getTimelineEvent().getData());
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
        }
    }
}
