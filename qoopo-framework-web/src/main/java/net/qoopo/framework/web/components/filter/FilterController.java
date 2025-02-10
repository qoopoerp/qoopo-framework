package net.qoopo.framework.web.components.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Submenu;

import jakarta.faces.event.AjaxBehaviorEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.ConditionCollection;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.filter.core.condition.Function;
import net.qoopo.framework.filter.core.condition.Value;
import net.qoopo.framework.models.OpcionBase;

/**
 * Gestor de filtros
 *
 * @author alberto
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FilterController implements Serializable {

    public static final Logger log = Logger.getLogger("FilterController");

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final List<Condition> previas = new ArrayList<>();
    private final List<Condition> sugerencias = new ArrayList<>();
    @Builder.Default
    private List<Condition> condiciones = new ArrayList<>(); // las condiciones del filtro
    private List<Field> camposDisponibles; // los campos disponibles para la busqueda en la function de completar
    private List<OpcionBase> camposGrupo; // los campos por los que se puede agrupar
    private OpcionBase campoGrupo = OpcionBase.SIN_GRUPO;
    private Filter filtro;
    /**
     * el método que se llamará cuando se procese le filtro, generalmente cargar la
     * lista
     */
    private Accion accion;
    /**
     * La condición permanente que se debe aplicar siempre, a pesar que seleccione
     * otros Filtros. Por ejemplo que solo muestre registros de una sola empresa,
     * o que estos registros sea de un solo tipo
     */
    private Condition condicionPermanente;

    // filtros preconfigurados que se puedan mostar en la GUI
    private List<Condition> condicionesDisponibles = new ArrayList<>();

    private MenuModel conditionsMenuModel;

    private FilterController() {
        //
    }

    public FilterController(Filter filtroInicial, List<Field> camposDisponibles, Accion accion) {
        // if (filtroInicial == null) {
        // throw new QoopoException("El filtro inicial no puede ser nulo");
        // }
        this.filtro = filtroInicial;
        this.camposDisponibles = camposDisponibles;
        this.accion = accion;
        this.condicionPermanente = filtro.getCondition();
    }

    public FilterController(Filter filtroInicial, Condition condicionPermanente, List<Field> camposDisponibles,
            Accion accion) {
        this(filtroInicial, condicionPermanente, camposDisponibles, null, accion);
    }

    public FilterController(Filter filtroInicial, Condition condicionPermanente, List<Field> camposDisponibles,
            List<OpcionBase> camposGrupo, Accion accion) {
        this.filtro = filtroInicial;
        this.camposDisponibles = camposDisponibles;
        this.camposGrupo = camposGrupo;
        this.accion = accion;
        this.condicionPermanente = condicionPermanente;
    }

    public void appendCondition(Condition condicion) {
        if (condiciones == null) {
            condiciones = new ArrayList<>();
        }
        if (condicion != null)
            condiciones.add(condicion);
    }

    public void appendAvalaibleCondition(Condition condicion) {
        if (condicion != null) {
            condicionesDisponibles.add(condicion);
        }

    }

    public void buildConditionsMenu() {
        conditionsMenuModel = new DefaultMenuModel();
        for (Condition condition : condicionesDisponibles) {
            conditionsMenuModel.getElements().add(buildConditionMenuItem(condition));
        }
        conditionsMenuModel.generateUniqueIds();
    }

    private MenuElement buildConditionMenuItem(Condition condition) {
        try {
            if (condition instanceof ConditionCollection) {
                Submenu subMenu = DefaultSubMenu.builder().label(condition.getName())
                        .build();
                for (Condition child : ((ConditionCollection) condition).getItems())
                    subMenu.getElements().add(buildConditionMenuItem(child));
                return subMenu;
            } else {
                condition.setId(new SecureRandom().nextLong());
                return DefaultMenuItem.builder()
                        .value(condition.getName())
                        // .icon("pi pi-save")
                        // .ajax(false)
                        .command("#{cc.attrs.value.selectCondition(" + condition.getId() + ")}")
                        .update("@form")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Selecciona uno de los filtros preconfigurados
     *
     * @param filtro
     */
    public void seleccionarFiltro(Filter filtro) {
        seleccionarFiltroOnly(filtro);
        procesar();
    }

    /**
     * Seleciona uno filtro sin llamar al procesar
     *
     * @param filtro
     */
    public void seleccionarFiltroOnly(Filter filtro) {
        // log.info("--> Seleccionar filtro only " + filtro);
        if (condiciones == null) {
            condiciones = new ArrayList<>();
        }
        Condition condicion = filtro.getCondition();
        if (condicion != null) {
            condicion.setName(filtro.getName());
            condiciones.add(condicion);
        }
    }

    public void selectCondition(Long id) {
        try {
            if (condicionesDisponibles != null && !condicionesDisponibles.isEmpty()) {
                condicionesDisponibles.stream().forEach(c -> selectCondition(id, c));
            }
        } catch (Exception e) {
            log.severe("Error in select condition id, imprimiendo los que existen");
            // if (condicionesDisponibles != null && !condicionesDisponibles.isEmpty()) {
            // condicionesDisponibles.stream()
            // .forEach(c -> log.info("Condition id-> " + c.getId() + " name->" +
            // c.getName()));
            // }

            e.printStackTrace();

        }
    }

    private void selectCondition(Long id, Condition condition) {
        if (condition instanceof ConditionCollection) {
            ((ConditionCollection) condition).getItems().forEach(c -> selectCondition(id, c));

        } else {
            if (condition.getId().equals(id)) {
                seleccionarCondicion(condition);
            }
        }
    }

    /**
     * Selecciona uno de los filtros preconfigurados
     *
     * @param filtro
     */
    public void seleccionarCondicion(Condition condicion) {
        appendCondition(condicion);
        procesar();
    }

    public void procesar() {
        // log.info("[+] filter-controller: Procesar");
        try {
            if (this.condicionPermanente != null) {
                this.filtro.setCondition(this.condicionPermanente.clonar());
            } else {
                this.filtro.setCondition(null);
            }
            if (this.condiciones != null && !this.condiciones.isEmpty()) {
                for (Condition condicion : this.condiciones) {
                    if (condicion.isExclusivo()) {
                        // log.warning("El filto es exclusivo, elimino los filtros anteriores " +
                        // condicion);
                        this.filtro.setCondition(null);
                    }
                    this.filtro.appendCondition(condicion.clonar(), 1); // 1=And
                }
                Condition.CONDICIONES.addAll(this.condiciones);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            filtrar();
        }
    }

    public void limpiar() {
        condiciones = new ArrayList<>();
        procesar();
    }

    public void cambiar(AjaxBehaviorEvent event) {
        procesar();
    }

    public void seleccionar(SelectEvent<Condition> event) {
        procesar();
    }

    public void desSeleccionar(UnselectEvent<Condition> event) {
        procesar();
    }

    private void filtrar() {
        if (accion != null) {
            accion.ejecutar();
        }
    }

    public void elimiminarGrupo() {
        setCampoGrupo(OpcionBase.SIN_GRUPO);
        procesar();
    }

    public void agruparPor(OpcionBase field) {
        setCampoGrupo(field);
        procesar();
    }

    public void orderAsc() {
        filtro.setOrderDirection("asc");
        procesar();
    }

    public void orderDesc() {
        filtro.setOrderDirection("desc");
        procesar();
    }

    public List<Condition> completarFiltros(String query) {
        // log.info("---> Completando filtro");
        sugerencias.clear();
        List<Condition> temp = new ArrayList<>();
        if (previas != null && !previas.isEmpty()) {
            Condition.CONDICIONES.removeAll(previas);
        }
        // agrega las que estan actualmente seleccionadas
        for (Condition item : condiciones) {
            Condition.CONDICIONES.add(item);
        }
        query = query.trim();
        if (query.length() < 1) {
            return temp;
        }
        String strCriterio = "criterio";
        for (Field field : camposDisponibles) {
            try {
                switch (field.getTipo()) {
                    case Field.STRING:
                        temp.add(Condition.builder().field(field).function(Function.CONTIENE)
                                .value(new Value(strCriterio, "%" + query.toLowerCase().replace(" ", "%") + "%"))
                                .build());
                        temp.add(Condition.builder().field(field).function(Function.NO_CONTIENE)
                                .value(new Value(strCriterio, "%" + query.toLowerCase() + "%")).build());
                        break;
                    case Field.BOLEANO:
                        // LOS BOLEANOS SOLO ES VERDADERO O FALSO
                        temp.add(Condition.builder().field(field).function(Function.ES_VERDADERO).build());
                        temp.add(Condition.builder().field(field).function(Function.ES_FALSO).build());
                        break;
                    case Field.NUMERICO:
                        temp.add(Condition.builder().field(field).function(Function.MAYOR_QUE)
                                .value(new Value(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MENOR_QUE)
                                .value(new Value(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MAYOR_O_IGUAL_QUE)
                                .value(new Value(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MENOR_O_IGUAL_QUE)
                                .value(new Value(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.IGUAL)
                                .value(new Value(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.DIFERENTE)
                                .value(new Value(strCriterio, new BigDecimal(query))).build());
                        break;
                    case Field.INTEGER:
                        temp.add(Condition.builder().field(field).function(Function.MAYOR_QUE)
                                .value(new Value(strCriterio, Integer.valueOf(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MENOR_QUE)
                                .value(new Value(strCriterio, Integer.valueOf(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.IGUAL)
                                .value(new Value(strCriterio, Integer.valueOf(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.DIFERENTE)
                                .value(new Value(strCriterio, Integer.valueOf(query))).build());
                        break;
                    case Field.LONG:
                        temp.add(Condition.builder().field(field).function(Function.MAYOR_QUE)
                                .value(new Value(strCriterio, Long.valueOf(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MENOR_QUE)
                                .value(new Value(strCriterio, Long.valueOf(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.IGUAL)
                                .value(new Value(strCriterio, Long.valueOf(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.DIFERENTE)
                                .value(new Value(strCriterio, Long.valueOf(query))).build());
                        break;
                    case Field.FECHA:
                        temp.add(Condition.builder().field(field).function(Function.MAYOR_QUE)
                                .value(new Value(strCriterio, sdf.parse(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MENOR_QUE)
                                .value(new Value(strCriterio, sdf.parse(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MAYOR_O_IGUAL_QUE)
                                .value(new Value(strCriterio, sdf.parse(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.MENOR_O_IGUAL_QUE)
                                .value(new Value(strCriterio, sdf.parse(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.IGUAL)
                                .value(new Value(strCriterio, sdf.parse(query))).build());
                        temp.add(Condition.builder().field(field).function(Function.DIFERENTE)
                                .value(new Value(strCriterio, sdf.parse(query))).build());
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {

            }
        }

        // valido que entre las nuevas opciones no se encuentre una repetida en las ya
        // seleccionadas
        if (condiciones != null && !condiciones.isEmpty()) {
            boolean allMatch = temp.stream().filter(t -> !condiciones.contains(t)).allMatch(t -> sugerencias.add(t));
            if (!allMatch) {
                log.warning("filtro no match");
            }
        } else {
            sugerencias.addAll(temp);
        }

        previas.clear();
        previas.addAll(sugerencias);
        Condition.CONDICIONES.addAll(sugerencias);
        return temp;
    }

    public List<Condition> getCondiciones() {
        return condiciones;
    }

    /**
     * Desactivo el set de las condiciones, para gestionarlo manualmente con los
     * eventos de itemSelect y itemUnselect
     *
     * @param condiciones
     */
    public void setCondiciones(List<Condition> condiciones) {
        // log.info("[~] Seteando condiciones");
        if (condiciones == null) {
            condiciones = new ArrayList<>();
            // log.info("[~] Seteando condiciones -- Viene nulo");
        } else {
            /*
             * log.info("[~] Seteando condiciones --  size " + condiciones.size());
             * for (Condition item : condiciones) {
             * log.info("[~] condicion:" + item.getNombre());
             * }
             */
        }
        // comparo si las condiciones son diferentes a las actuales
        boolean actualizar = false;
        if (this.condiciones == null || (this.condiciones.size() != condiciones.size())
                || (!this.condiciones.containsAll(condiciones)) || (!this.condiciones.equals(condiciones))) {
            actualizar = true;
        }

        if (actualizar) {
            this.condiciones = condiciones;
            procesar();
        }
    }

}
