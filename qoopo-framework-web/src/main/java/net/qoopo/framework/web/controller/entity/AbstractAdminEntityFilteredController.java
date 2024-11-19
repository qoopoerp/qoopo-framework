package net.qoopo.framework.web.controller.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.schedule.ScheduleEntryMoveEvent;
import org.primefaces.event.schedule.ScheduleEntryResizeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.QoopoFramework;
import net.qoopo.framework.exporter.Exportable;
import net.qoopo.framework.exporter.Exporter;
import net.qoopo.framework.exporter.Importer;
import net.qoopo.framework.exporter.csv.CsvExporter;
import net.qoopo.framework.exporter.csv.CsvImporter;
import net.qoopo.framework.exporter.json.JsonExporter;
import net.qoopo.framework.exporter.json.JsonImporter;
import net.qoopo.framework.exporter.xls.XlsExporter;
import net.qoopo.framework.exporter.xls.XlsImporter;
import net.qoopo.framework.exporter.xlsx.XlsxExporter;
import net.qoopo.framework.exporter.xlsx.XlsxImporter;
import net.qoopo.framework.filter.FilterJpaRepository;
import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.GeneralFilter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.jpa.core.interfaces.Agrupable;
import net.qoopo.framework.jpa.core.interfaces.Auditable;
import net.qoopo.framework.jpa.core.interfaces.Eventable;
import net.qoopo.framework.jpa.core.interfaces.Graficable;
import net.qoopo.framework.lang.LanguageProvider;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.multitenant.MultitenantFilter;
import net.qoopo.framework.multitenant.TenantProvider;
import net.qoopo.framework.reports.Reporte;
import net.qoopo.framework.util.QLogger;
import net.qoopo.framework.web.AppSessionBeanInterface;
import net.qoopo.framework.web.ImagenesBean;
import net.qoopo.framework.web.SecurityContextBean;
import net.qoopo.framework.web.components.filter.FilterController;
import net.qoopo.framework.web.components.graph.GraphController;
import net.qoopo.framework.web.components.kanban.ColumnDragDrop;
import net.qoopo.framework.web.components.kanban.KanbanColumn;
import net.qoopo.framework.web.components.nav.NavController;
import net.qoopo.framework.web.components.tree.TreeController;
import net.qoopo.framework.web.components.viewoption.ViewOption;
import net.qoopo.framework.web.controller.AbstractAdminController;
import net.qoopo.framework.web.util.FacesUtils;
import net.qoopo.framework.web.vistas.ChatterInterface;
import net.qoopo.framework.web.vistas.ReporteBean;

/**
 * Controlador web para JSF que gestiona el proceso CRUD de entidades que
 * heredan de AbstractEntity
 */
@Getter
@Setter
public abstract class AbstractAdminEntityFilteredController<Entity extends AbstractEntity, EntityData extends AbstractEntity, EntityID>
        extends AbstractAdminController<Entity, EntityData, EntityID> {

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

    // Entidades
    // indica si se puede archivar, como una factura no tiene sentido archivarls
    protected boolean canArchive = true;

    protected ChatterInterface chatter;

    protected ScheduleEvent<EntityData> event = new DefaultScheduleEvent<>();
    protected TimelineModel<EntityData, Object> timeLineModel = new TimelineModel<>();
    protected TimelineEvent<EntityData> eventTimeLine;
    protected LocalDateTime timeLineStart;
    protected LocalDateTime timeLineEnd;

    protected transient Reporte reporte;
    protected transient StreamedContent contenidoExportar;
    protected transient InputStream inputArchivoImport;
    protected GraphController<EntityData> graph = null;
    protected TreeController tree = null;
    protected int count;
    protected Integer progress;
    protected String progressStatus;

    protected Exporter exporter = new CsvExporter();

    protected Importer importer = new CsvImporter();

    protected int importerType = 1;

    /**
     * Se puede establecer el nombre del archivo a exportar, en caso de no definirlo
     * se usa el nombre de la clase de la entidad
     */
    protected String exportedNameFile = null;
    protected int exporterType = 1;

    // datos que se muestran actualmente
    protected List<KanbanColumn> columnas = new ArrayList<>(); // para la vista de kanban
    protected ColumnDragDrop columnDragDrop = new ColumnDragDrop();

    protected ScheduleModel eventModel = new DefaultScheduleModel();

    // Filtros

    protected FilterController filter;

    protected Filter inicial = null;

    protected Condition condicionFija = null;

    protected String filterName = "";

    protected FilterJpaRepository<EntityData> filterRepository;

    /**
     * Los campos que van a estar disponibles en el filtro
     */
    protected List<Field> campos = new ArrayList<>();

    protected final List<OpcionBase> opcionesGrupos = new ArrayList<>();

    protected final List<Condition> condicionesDisponibles = new ArrayList<>();

    /**
     * El nombre de la clase Jpa de la entidad a administra. Es el classname de T,
     * el cual no puede ser obtenido antes de tener datos, pero es necesario en el
     * momento de buildFilter, antes de cargar los datos
     */
    protected final String entityClassName;
    protected final Class<EntityData> entityClass;

    protected NavController nav = new NavController(new Accion() {
        @Override
        public Object ejecutar(Object... JpaParameterss) {
            seleccionar((Integer) JpaParameterss[0]);
            return null;
        }
    }, new Accion() {
        @Override
        public Object ejecutar(Object... JpaParameterss) {
            return getTotal();
        }
    });

    /**
     * Accion que se ejecuta para la actualizacion del valor de progress
     *
     * Ejemplo de uso new Thread(() -> {
     * InventarioQuants.calculateQuants(empresa, accionUpdateProgress);
     * }).start();
     */
    protected Accion accionUpdateProgress = new Accion() {
        @Override
        public Object ejecutar(Object... JpaParameterss) {
            setProgress((Integer) JpaParameterss[0]);
            if (JpaParameterss.length > 1) {
                setProgressStatus((String) JpaParameterss[1]);
            }
            return (Integer) JpaParameterss[0];
        }
    };

    /**
     * Accion que se ejecuta en los filtros, predeterminado cargar lista
     */
    protected Accion accion = new Accion() {
        @Override
        public Object ejecutar(Object... JpaParameterss) {
            loadData();
            if (sessionBean != null && viewOption != null) {
                sessionBean.addUrlParam("view", viewOption.getStringValue());
            }
            return null;
        }
    };

    protected final ViewOption viewOption = new ViewOption(accion);

    protected abstract void initChatter();

    public abstract void importar();

    public AbstractAdminEntityFilteredController(String entityClassName, Class<EntityData> entityClass) {
        this.entityClassName = entityClassName;
        this.entityClass = entityClass;
    }

    @PostConstruct
    public void postConstruct() {
        graph = new GraphController<EntityData>(languageProvider);
        tree = new TreeController();
        initChatter();
    }

    protected void loadData(Iterable<EntityData> data) {
        if (listaSeleccionados != null)
            listaSeleccionados.clear();
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

    /**
     * Configura los filtros genericos en caso que no se definan personalizaciones
     */
    public void buildFilter() {
        try {
            Filter _inicial = null;

            if (this.inicial == null)
                _inicial = GeneralFilter.all(entityClassName);
            else
                _inicial = inicial;

            Condition alwaysCondition = null;

            if (this.condicionFija != null)
                alwaysCondition = this.condicionFija;
            else {
                if (QoopoFramework.get().getMultitenantConfigurer().isEnabled())
                    alwaysCondition = MultitenantFilter.tenantCondition(
                            tenantProvider.getTenantId());
            }

            filter = new FilterController(_inicial, alwaysCondition, campos,
                    this.opcionesGrupos,
                    accion);

            if (condicionesDisponibles != null && !condicionesDisponibles.isEmpty()) {
                condicionesDisponibles.forEach(c -> filter.appendAvalaibleCondition(c));
            }
            if (canArchive) {
                filter.appendAvalaibleCondition(GeneralFilter.condicionActivo());
                filter.appendAvalaibleCondition(GeneralFilter.condicionArchivado());
            }
            filter.seleccionarFiltroOnly(_inicial);
            if (canArchive)
                filter.appendCondition(GeneralFilter.condicionActivo()); // muestra inicialmente los no archivados
            filter.procesar();// esta linea termina llamando al metodo loadData
        } catch (Exception e) {
            e.printStackTrace();
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
            if (validateArchive(objeto)) {
                objeto.setArchived(true);
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
            objeto.setArchived(false);
            if (objeto instanceof Auditable) {
                // solo agrega un metadato en caso que no exista uno
                if (((Auditable) objeto).getMetadato() == null) {
                    ((Auditable) objeto).setMetadato(sessionBean.addEvent(((Auditable) objeto).getMetadato(),
                            "Desarchivado"));
                }
                chatter.saveProperties();
            }
            update();
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
        // DefaultScheduleEvent.builder().
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

    public void updateExporter() {
        log.info("actualizando exporter " + exporterType);
        switch (exporterType) {
            case 1:
                exporter = new CsvExporter();
                break;
            case 2:
                exporter = new XlsExporter();
                break;
            case 3:
                exporter = new XlsxExporter();
                break;
            case 4:
                exporter = new JsonExporter();
                break;
        }
    }

    public void updateImporter() {
        log.info("actualizando importer " + importerType);
        switch (importerType) {
            case 1:
                importer = new CsvImporter();
                break;
            case 2:
                importer = new XlsImporter();
                break;
            case 3:
                importer = new XlsxImporter();
                break;
            case 4:
                importer = new JsonImporter();
                break;
        }
    }

    public void exportar() {
        log.warning("[!] Exportando [" + getClass().getName() + "]");
        try {
            // exportar(exporter, data, getClass().getSimpleName() + ".csv");
            String fileName = null;
            if (exportedNameFile != null)
                fileName = exportedNameFile;
            else
                fileName = data.iterator().next().getClass().getSimpleName();
            exportar(exporter, data, fileName);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Error al exportar " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void exportar(Exporter exporter, Iterable<EntityData> data, String nombre) {
        try {
            exporter.clear();
            byte[] datos;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            exporter.setOutputExporter(bos);
            for (Object oitem : data) {
                if (oitem instanceof Exportable) {
                    Exportable item = (Exportable) oitem;
                    exporter.startItem();
                    item.exportar(exporter);
                    exporter.endItem();
                }
            }
            exporter.exportar();
            datos = bos.toByteArray();
            bos.close();
            InputStream is = new ByteArrayInputStream(datos);
            contenidoExportar = DefaultStreamedContent.builder().contentType(exporter.getMimetype())
                    .name(nombre + exporter.getExtension())
                    .stream(() -> is).build();
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Error al exportar " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


}
