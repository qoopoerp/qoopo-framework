package net.qoopo.framework.web.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.QoopoFramework;
import net.qoopo.framework.filter.FilterJpaRepository;
import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.GeneralFilter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.ConditionCollection;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.multitenant.MultitenantFilter;
import net.qoopo.framework.multitenant.TenantProvider;
import net.qoopo.framework.web.components.filter.FilterController;

/**
 * Controlador web para JSF que gestiona el proceso CRUD de entidades que
 * heredan de AbstractEntity
 */
@Getter
@Setter
public abstract class AbstractCrudFilteredController<Entity, EntityData, EntityID>
        extends AbstractCrudExportableController<Entity, EntityData, EntityID> {

    @Inject
    protected TenantProvider tenantProvider;

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

    protected boolean canArchive = true;

    /**
     * Accion que se ejecuta en los filtros, predeterminado cargar lista
     */
    protected Accion accion = new Accion() {
        @Override
        public Object ejecutar(Object... parameters) {
            loadData();
            // if (sessionBean != null && viewOption != null) {
            // sessionBean.addUrlParam("view", viewOption.getStringValue());
            // }
            return null;
        }
    };

    /**
     * El nombre de la clase Jpa de la entidad a administra. Es el classname de T,
     * el cual no puede ser obtenido antes de tener datos, pero es necesario en el
     * momento de buildFilter, antes de cargar los datos
     */
    protected final String entityClassName;

    public AbstractCrudFilteredController(String entityClassName) {
        this.entityClassName = entityClassName;
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
                filter.appendAvalaibleCondition(ConditionCollection.of("Archivado",
                        List.of(GeneralFilter.condicionActivo(), GeneralFilter.condicionArchivado())));
                // filter.appendAvalaibleCondition(GeneralFilter.condicionActivo());
                // filter.appendAvalaibleCondition(GeneralFilter.condicionArchivado());
            }
            if ((condicionesDisponibles != null && !condicionesDisponibles.isEmpty())
                    || canArchive) {
                filter.buildConditionsMenu();
            }

            filter.seleccionarFiltroOnly(_inicial);
            if (canArchive)
                filter.appendCondition(GeneralFilter.condicionActivo()); // muestra inicialmente los no archivados
            filter.procesar();// esta linea termina llamando al metodo loadData
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
