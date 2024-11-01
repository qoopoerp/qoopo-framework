package net.qoopo.framework.web.core.dto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.schedule.ScheduleEntryMoveEvent;
import org.primefaces.event.schedule.ScheduleEntryResizeEvent;
import org.primefaces.event.timeline.TimelineModificationEvent;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.db.repository.CrudRepository;
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
import net.qoopo.framework.jpa.core.EntidadBase;
import net.qoopo.framework.jpa.core.dtos.DtoBase;
import net.qoopo.framework.jpa.core.interfaces.Agrupable;
import net.qoopo.framework.jpa.core.interfaces.Auditable;
import net.qoopo.framework.jpa.core.interfaces.CoreMetadata;
import net.qoopo.framework.jpa.core.interfaces.Eventable;
import net.qoopo.framework.jpa.core.interfaces.Graficable;
import net.qoopo.framework.jpa.core.interfaces.Ordenable;
import net.qoopo.framework.jpa.filter.Filter;
import net.qoopo.framework.jpa.filter.GeneralFilter;
import net.qoopo.framework.jpa.filter.condicion.Campo;
import net.qoopo.framework.jpa.filter.condicion.Condicion;
import net.qoopo.framework.jpa.filter.condicion.Valor;
import net.qoopo.framework.lang.LanguageProvider;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.reports.Reporte;
import net.qoopo.framework.repository.FilterJpaRepository;
import net.qoopo.framework.repository.QoopoJpaRepositorySingleton;
import net.qoopo.framework.util.QLogger;
import net.qoopo.framework.util.QoopoUtil;
import net.qoopo.framework.web.AppSessionBeanInterface;
import net.qoopo.framework.web.ImagenesBean;
import net.qoopo.framework.web.components.filter.FilterController;
import net.qoopo.framework.web.components.graph.GraphController;
import net.qoopo.framework.web.components.kanban.ColumnDragDrop;
import net.qoopo.framework.web.components.kanban.KanbanColumn;
import net.qoopo.framework.web.components.nav.NavController;
import net.qoopo.framework.web.components.tree.TreeController;
import net.qoopo.framework.web.components.viewoption.ViewOption;
import net.qoopo.framework.web.core.interfaces.AdminBeanProgressable;
import net.qoopo.framework.web.util.FacesUtils;
import net.qoopo.framework.web.vistas.ChatterInterface;
import net.qoopo.framework.web.vistas.ReporteBean;

/**
 * Clase de esqueleto de los beans de administración
 *
 * @author alberto
 * @param <T>
 */

@Getter
@Setter
public abstract class AdminDtoAbstractClass<S extends EntidadBase, T extends DtoBase>
        implements AdminBeanProgressable, Serializable {

    public static final Logger log = Logger.getLogger("Qoopo");

    @Inject
    protected AppSessionBeanInterface sessionBean;

    // @Inject
    // protected QoopoFormater formatter;

    @Inject
    protected LanguageProvider languageProvider;

    @Inject
    protected ReporteBean reporteBean;

    @Inject
    protected ImagenesBean imagenesBean;

    protected ChatterInterface chatter;

    protected S objeto = null;
    protected List<T> listaSeleccionados = new ArrayList<>();
    protected Iterable<T> data = new ArrayList<>(); // en los beans que tienen carga diferida, este corresponde a los
    // datos que se muestran actualmente
    protected List<KanbanColumn> columnas = new ArrayList<>(); // para la vista de kanban
    protected ColumnDragDrop columnDragDrop = new ColumnDragDrop();
    protected boolean editando;
    protected FilterController filter;
    protected ScheduleModel eventModel = new DefaultScheduleModel();
    protected ScheduleEvent event = new DefaultScheduleEvent<>();
    protected TimelineModel<T, Object> timeLineModel = new TimelineModel<>();
    protected TimelineEvent<T> eventTimeLine;
    protected LocalDateTime timeLineStart;
    protected LocalDateTime timeLineEnd;
    protected transient Reporte reporte;
    protected transient StreamedContent contenidoExportar;
    protected transient InputStream inputArchivoImport;
    protected GraphController<T> graph = null;
    protected TreeController tree = null;
    protected int count;
    protected Integer progress;
    protected String progressStatus;

    // indica si se puede archivar, como una factura no tiene sentido archivarls
    protected boolean canArchive = true;

    protected Exporter exporter = new CsvExporter();

    protected Importer importer = new CsvImporter();

    protected int importerType = 1;
    /**
     * Se puede establecer el nombre del archivo a exportar, en caso de no definirlo
     * se usa el nombre de la clase de la entidad
     */
    protected String exportedNameFile = null;
    protected int exporterType = 1;

    /**
     * El nombre de la clase JPA de la entidad a administra. Es el classname de T,
     * el cual no puede ser obtenido antes de tener datos, pero es necesario en el
     * momento de buildFilter, antes de cargar los datos
     */
    protected String entityClassName = "";

    protected final Class<S> entityClass;

    /**
     * Los campos que van a estar disponibles en el filtro
     */
    protected List<Campo> campos = new ArrayList<>();

    protected final List<OpcionBase> opcionesGrupos = new ArrayList<>();

    protected final List<Condicion> condicionesDisponibles = new ArrayList<>();

    protected Filter inicial = null;

    protected Condicion condicionFija = null;

    protected String filterName = "";

    protected boolean entitiesLoaded = false;

    protected boolean masivo = false;

    protected FilterJpaRepository<T> filterRepository;

    protected CrudRepository<S, Long> repository;

    /**
     * Transforma el campo del dto al campo de la entidad para agregar en el sortby
     * (Necesario para los LazyModel)
     * del sql
     */
    public String getSortField(String field) {
        return field;
    }

    protected NavController nav = new NavController(new Accion() {
        @Override
        public Object ejecutar(Object... parametros) {
            seleccionar((Integer) parametros[0]);
            return null;
        }
    }, new Accion() {
        @Override
        public Object ejecutar(Object... parametros) {
            return getTotal();
        }
    });
    /**
     * Accion que se ejecuta en los filtros, predeterminado cargar lista
     */
    protected Accion accion = new Accion() {
        @Override
        public Object ejecutar(Object... parametros) {
            loadData();
            if (sessionBean != null && viewOption != null) {
                sessionBean.addUrlParam("view", viewOption.getStringValue());
            }
            return null;
        }
    };

    protected final ViewOption viewOption = new ViewOption(accion);

    /**
     * Accion que se ejecuta para la actualizacion del valor de progress
     *
     * Ejemplo de uso new Thread(() -> {
     * InventarioQuants.calculateQuants(empresa, accionUpdateProgress);
     * }).start();
     */
    protected Accion accionUpdateProgress = new Accion() {
        @Override
        public Object ejecutar(Object... parametros) {
            setProgress((Integer) parametros[0]);
            if (parametros.length > 1) {
                setProgressStatus((String) parametros[1]);
            }
            return (Integer) parametros[0];
        }
    };

    public AdminDtoAbstractClass(String entityClassName, Class<S> entityClass, Filter inicial,
            List<Condicion> condicionesDisponibles,
            List<Campo> campos,
            List<OpcionBase> opcionesGrupos) {
        this.entityClassName = entityClassName;
        this.inicial = inicial;
        this.campos.addAll(campos);
        this.opcionesGrupos.addAll(opcionesGrupos);
        this.condicionesDisponibles.addAll(condicionesDisponibles);
        this.entityClass = entityClass;
        log.info("AdminDtoabstractClass ->" + entityClassName);
    }

    protected abstract void initChatter();

    public abstract void initObjeto();

    public abstract void loadData();

    public abstract int getTotal();

    public boolean validateDelete(EntidadBase item) {
        return true;
    }

    public boolean validateArchive(EntidadBase item) {
        return true;
    }

    public void postDelete(EntidadBase item) {
        //
    }

    @PostConstruct
    public void postConstruct() {
        graph = new GraphController<T>(languageProvider);
        tree = new TreeController();
        initChatter();
        // chatter = new Chatter(sessionBean, languageProvider);
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
                            .filter(c -> c.getNombre().equals(filterName))
                            .collect(Collectors.toList())
                            .forEach(c1 -> {
                                Condicion c2 = c1.clonar();
                                if (filterValue != null && !filterValue.isEmpty()) {
                                    switch (c2.getCampo().getTipo()) {
                                        case Campo.INTEGER:
                                            c2.setValor1(new Valor(Integer.valueOf(filterValue)));
                                            break;
                                        case Campo.LONG:
                                            c2.setValor1(new Valor(Long.valueOf(filterValue)));
                                            break;
                                        case Campo.BOLEANO:
                                            c2.setValor1(new Valor(Boolean.valueOf(filterValue)));
                                            break;
                                        case Campo.NUMERICO:
                                            c2.setValor1(new Valor(new BigDecimal(filterValue)));
                                            break;
                                        case Campo.FECHA:
                                            c2.setValor1(new Valor(
                                                    LocalDateTime.from(QoopoUtil.getSDF().parse(filterValue))));
                                            break;
                                        case Campo.STRING:
                                        default:
                                            c2.setValor1(new Valor(filterValue));
                                            break;
                                    }
                                    c2.setNombre(filterName + " = " + filterValue);
                                    log.info("[+] cambiando valor del filtro " + filterName + " a " + filterValue);
                                }
                                // filter.limpiar();
                                filter.seleccionarCondicion(c2);
                            });
                }
            }
            // parametro del tipo vista
            if (FacesUtils.getRequestParameter("view") != null) {
                viewOption.setValue(FacesUtils.getRequestParameter("view"));
            }

            // carga un objeto con el id del parametro
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

    public void clonar() {
        try {
            S temp = (S) objeto.clonar();
            if (objeto instanceof Auditable) {
                if (((Auditable) temp).getMetadato() == null) {
                    ((Auditable) temp).setMetadato(sessionBean.addEvent(((Auditable) temp).getMetadato(),
                            "Clonado de " + objeto.toString()));
                }
                chatter.saveProperties();
            }
            nuevo();
            this.objeto = temp;
        } catch (Exception e) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue(1649));
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

            Condicion _condicionFija = null;

            if (this.condicionFija != null)
                _condicionFija = this.condicionFija;
            else
                _condicionFija = GeneralFilter.condicionEmpresa(sessionBean.getEmpresaId());

            filter = new FilterController(_inicial, _condicionFija, campos,
                    this.opcionesGrupos,
                    accion);

            if (condicionesDisponibles != null && !condicionesDisponibles.isEmpty()) {
                condicionesDisponibles.forEach(c -> filter.agregarCondicionDisponible(c));
            }
            if (canArchive) {
                filter.agregarCondicionDisponible(GeneralFilter.condicionActivo());
                filter.agregarCondicionDisponible(GeneralFilter.condicionArchivado());
            }
            filter.seleccionarFiltroOnly(_inicial);
            if (canArchive)
                filter.agregarCondicion(GeneralFilter.condicionActivo()); // muestra inicialmente los no archivados
            filter.procesar();// esta linea termina llamando al metodo loadData
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
        if (listaSeleccionados != null)
            listaSeleccionados.clear();
        long tInicio = System.currentTimeMillis();
        entitiesLoaded = false;
        setData(data);
        loadEvents(data);
        loadGraph(data);
        loadTree(data);
        loadKanban(data);
        loadTimeLine(data);
        // actualizo el parametro del view
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
    private void loadEvents(Iterable<T> lista) {
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
    private void loadTree(Iterable<T> lista) {
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
    private void loadGraph(Iterable<T> lista) {
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
    private TimelineEvent createTimeLineEvent(Eventable ob, String grupo) {
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

    private void loadTimeLine(Iterable<T> lista) {
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
                repository.delete(item);
                postDelete(item);
                loadData();
                FacesUtils.addInfoMessage(languageProvider.getTextValue(22));
                // en caso de estar mostrando el objeto, debe regresar a la vista default
                viewOption.reset();
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue(20) + ex.getMessage());
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
                repository.delete(objeto);
                postDelete(objeto);
                loadData();
                if (nav.getActual() >= getTotal()) {
                    nav.setActual(getTotal() - 1);
                }
                seleccionar(nav.getActual());
                FacesUtils.addInfoMessage(languageProvider.getTextValue(22));
                viewOption.reset();
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue(20) + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
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
            FacesUtils.addErrorMessage(languageProvider.getTextValue(20) + ex.getMessage());
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
            FacesUtils.addErrorMessage(languageProvider.getTextValue(20) + ex.getMessage());
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
            objeto = (S) repository.save(objeto);
            loadData();
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
            objeto = (S) repository.save(objeto);
            // loadData();
            Optional<S> tmp = repository.find(objeto.getId());
            if (tmp.isPresent())
                objeto = tmp.get();
            else
                log.severe("no se encontro el objeto deespues de actualizar");
            editItem(objeto);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Método que debe ser llamado cuando se presione el botón "Crear" de las
     * plantillas
     */
    public void nuevo() {
        log.info("dto call new -> " + entityClassName);
        initObjeto();
        sessionBean.removeUrlParam("id");
        viewOption.setValue(ViewOption.FORM);
        sessionBean.addUrlParam("view", "form");
        editando = false;
        if (objeto instanceof Auditable) {
            chatter.mostrar(((Auditable) objeto).getMetadato(), (Auditable) objeto);
        } else {
            chatter.mostrar(null, null);
        }
    }

    public void deleteSelected() {
        try {
            StringBuilder sb = new StringBuilder();
            boolean error = false;
            masivo = true;
            if (listaSeleccionados != null && !listaSeleccionados.isEmpty()) {
                for (T item : listaSeleccionados) {
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
                editItem((S) (EntidadBase) item);
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
        log.info("editItem ->" + entityClassName + " -> " + item);
        // cambio la url para mostrar el id actual
        if (item != null && !masivo) {
            sessionBean.addUrlParam("id", String.valueOf(item.getId()));
            sessionBean.addUrlParam("view", "form");
        }
        setObjeto(item);
        editando = true;
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

    /**
     * Método interno usado por los botones de navegación (anterior, siguiente,
     * etc)
     *
     * @param indice
     */
    public abstract void seleccionar(int indice);

    /**
     * Devuelve la lista de los objetos seleccionados
     *
     * @return
     */
    public List<T> getListaSeleccionados() {
        return listaSeleccionados;
    }

    /**
     * Recibe la lista de los objetos seleccionados
     *
     * @param listaSeleccionados
     */
    public void setListaSeleccionados(List<T> listaSeleccionados) {
        this.listaSeleccionados = listaSeleccionados;
    }

    /**
     * Devuelve el objeto, si no se ha seleccionado ningún devuelve el primer
     * objeto de la lista, si no hay una lista crea un nuevo objeto listo para
     * ser guardado
     *
     * @return
     */
    public S getObjeto() {
        if (objeto == null) {
            if (getTotal() > 0) {
                this.seleccionar(1);
            } else {
                this.initObjeto();
            }
        }
        return objeto;
    }

    /**
     * Setea el objeto, solo uso interno, se debe usar edit para ser llamado
     * externamente
     *
     * @param objeto
     */
    public void setObjeto(S objeto) {
        this.objeto = objeto;
    }

    /**
     * Devuelve el procesado de filtros
     *
     * @return
     */
    public FilterController getFilter() {
        return filter;
    }

    /**
     * Recibe el procesador de filtros
     *
     * @param filter
     */
    public void setFilter(FilterController filter) {
        this.filter = filter;

    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
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

    public void onEventSelect(SelectEvent selectEvent) {
        try {
            edit(((T) ((ScheduleEvent) selectEvent.getObject()).getData()));
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void onTimeLineSelect(TimelineSelectEvent e) {
        try {
            edit((T) e.getTimelineEvent().getData());
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
        }
    }

    public void onTimeLineEdit(TimelineModificationEvent e) {
        try {
            edit((T) e.getTimelineEvent().getData());
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
        }
    }

    public void onTimeLineDelete(TimelineModificationEvent e) {
        try {
            edit((T) e.getTimelineEvent().getData());
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(ex);
        }
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
            loadEntities();
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

    public void exportar(Exporter exporter, Iterable<T> data, String nombre) {
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

    public void archivoSubido(FileUploadEvent event) {
        try {
            this.inputArchivoImport = event.getFile().getInputStream();
            FacesUtils.addInfoMessage(event.getFile().getFileName() + "  fue subido.");
        } catch (IOException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            FacesUtils.addErrorMessage("Error al cargar archivo.");
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
        // FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Row Moved",
        // "From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        // FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    @Override
    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
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
