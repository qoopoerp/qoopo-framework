package net.qoopo.framework.web.components.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import jakarta.faces.event.AjaxBehaviorEvent;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.jpa.filter.Filter;
import net.qoopo.framework.jpa.filter.condicion.Campo;
import net.qoopo.framework.jpa.filter.condicion.Condicion;
import net.qoopo.framework.jpa.filter.condicion.Funcion;
import net.qoopo.framework.jpa.filter.condicion.Valor;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.util.QoopoBuilder;

/**
 * Gestor de filtros
 *
 * @author alberto
 */
@Getter
@Setter
public class FilterController implements Serializable {

    public static final Logger log = Logger.getLogger("Qoopo");

    public static Builder getBuilder() {
        return new Builder();
    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final List<Condicion> previas = new ArrayList<>();
    private final List<Condicion> sugerencias = new ArrayList<>();
    private List<Condicion> condiciones = new ArrayList<>(); // las condiciones del filtro
    private List<Campo> camposDisponibles; // los campos disponibles para la busqueda en la funcion de completar
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
    private Condicion condicionPermanente;

    // filtros preconfigurados que se puedan mostar en la GUI
    private List<Condicion> condicionesDisponibles = new ArrayList<>();

    private FilterController() {
        //
    }

    public FilterController(Filter filtroInicial, List<Campo> camposDisponibles, Accion accion) {
        // if (filtroInicial == null) {
        // throw new QoopoException("El filtro inicial no puede ser nulo");
        // }
        this.filtro = filtroInicial;
        this.camposDisponibles = camposDisponibles;
        this.accion = accion;
        this.condicionPermanente = filtro.getCondicion();
    }

    public FilterController(Filter filtroInicial, Condicion condicionPermanente, List<Campo> camposDisponibles,
            Accion accion) {
        this(filtroInicial, condicionPermanente, camposDisponibles, null, accion);
    }

    public FilterController(Filter filtroInicial, Condicion condicionPermanente, List<Campo> camposDisponibles,
            List<OpcionBase> camposGrupo, Accion accion) {
        this.filtro = filtroInicial;
        this.camposDisponibles = camposDisponibles;
        this.camposGrupo = camposGrupo;
        this.accion = accion;
        this.condicionPermanente = condicionPermanente;
    }

    public void agregarCondicion(Condicion condicion) {
        if (condiciones == null) {
            condiciones = new ArrayList<>();
        }
        if (condicion != null)
            condiciones.add(condicion);
    }

    public void agregarCondicionDisponible(Condicion condicion) {
        if (condicion != null)
            condicionesDisponibles.add(condicion);
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
        Condicion condicion = filtro.getCondicion();
        if (condicion != null) {
            condicion.setNombre(filtro.getNombre());
            condiciones.add(condicion);
        }
    }

    /**
     * Selecciona uno de los filtros preconfigurados
     *
     * @param filtro
     */
    public void seleccionarCondicion(Condicion condicion) {
        agregarCondicion(condicion);
        procesar();
    }

    public void procesar() {
        // log.info("[+] filter-controller: Procesar");
        try {
            if (this.condicionPermanente != null) {
                this.filtro.setCondicion(this.condicionPermanente.clonar());
            } else {
                this.filtro.setCondicion(null);
            }
            if (this.condiciones != null && !this.condiciones.isEmpty()) {
                for (Condicion condicion : this.condiciones) {
                    if (condicion.isExclusivo()) {
                        // log.warning("El filto es exclusivo, elimino los filtros anteriores " +
                        // condicion);
                        this.filtro.setCondicion(null);
                    }
                    this.filtro.agregarCondicion(condicion.clonar(), 1);
                }
                Condicion.CONDICIONES.addAll(this.condiciones);
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

    public void seleccionar(SelectEvent<Condicion> event) {
        procesar();
    }

    public void desSeleccionar(UnselectEvent<Condicion> event) {
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

    public void agruparPor(OpcionBase campo) {
        setCampoGrupo(campo);
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

    public List<Condicion> completarFiltros(String query) {
        // log.info("---> Completando filtro");
        sugerencias.clear();
        List<Condicion> temp = new ArrayList<>();
        if (previas != null && !previas.isEmpty()) {
            Condicion.CONDICIONES.removeAll(previas);
        }
        // agrega las que estan actualmente seleccionadas
        for (Condicion item : condiciones) {
            Condicion.CONDICIONES.add(item);
        }
        query = query.trim();
        if (query.length() < 1) {
            return temp;
        }
        String strCriterio = "criterio";
        for (Campo campo : camposDisponibles) {
            try {
                switch (campo.getTipo()) {
                    case Campo.STRING:
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.CONTIENE)
                                .valor(new Valor(strCriterio, "%" + query.toLowerCase().replace(" ", "%") + "%"))
                                .build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.NO_CONTIENE)
                                .valor(new Valor(strCriterio, "%" + query.toLowerCase() + "%")).build());
                        break;
                    case Campo.BOLEANO:
                        // LOS BOLEANOS SOLO ES VERDADERO O FALSO
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.ES_VERDADERO).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.ES_FALSO).build());
                        break;
                    case Campo.NUMERICO:
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MAYOR_QUE)
                                .valor(new Valor(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MENOR_QUE)
                                .valor(new Valor(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MAYOR_O_IGUAL_QUE)
                                .valor(new Valor(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MENOR_O_IGUAL_QUE)
                                .valor(new Valor(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.IGUAL)
                                .valor(new Valor(strCriterio, new BigDecimal(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.DIFERENTE)
                                .valor(new Valor(strCriterio, new BigDecimal(query))).build());
                        break;
                    case Campo.INTEGER:
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MAYOR_QUE)
                                .valor(new Valor(strCriterio, Integer.valueOf(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MENOR_QUE)
                                .valor(new Valor(strCriterio, Integer.valueOf(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.IGUAL)
                                .valor(new Valor(strCriterio, Integer.valueOf(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.DIFERENTE)
                                .valor(new Valor(strCriterio, Integer.valueOf(query))).build());
                        break;
                    case Campo.LONG:
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MAYOR_QUE)
                                .valor(new Valor(strCriterio, Long.valueOf(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MENOR_QUE)
                                .valor(new Valor(strCriterio, Long.valueOf(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.IGUAL)
                                .valor(new Valor(strCriterio, Long.valueOf(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.DIFERENTE)
                                .valor(new Valor(strCriterio, Long.valueOf(query))).build());
                        break;
                    case Campo.FECHA:
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MAYOR_QUE)
                                .valor(new Valor(strCriterio, sdf.parse(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MENOR_QUE)
                                .valor(new Valor(strCriterio, sdf.parse(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MAYOR_O_IGUAL_QUE)
                                .valor(new Valor(strCriterio, sdf.parse(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.MENOR_O_IGUAL_QUE)
                                .valor(new Valor(strCriterio, sdf.parse(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.IGUAL)
                                .valor(new Valor(strCriterio, sdf.parse(query))).build());
                        temp.add(Condicion.getBuilder().campo(campo).funcion(Funcion.DIFERENTE)
                                .valor(new Valor(strCriterio, sdf.parse(query))).build());
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
        Condicion.CONDICIONES.addAll(sugerencias);
        return temp;
    }

    public List<Condicion> getCondiciones() {
        return condiciones;
    }

    /**
     * Desactivo el set de las condiciones, para gestionarlo manualmente con los
     * eventos de itemSelect y itemUnselect
     *
     * @param condiciones
     */
    public void setCondiciones(List<Condicion> condiciones) {
        // log.info("[~] Seteando condiciones");
        if (condiciones == null) {
            condiciones = new ArrayList<>();
            // log.info("[~] Seteando condiciones -- Viene nulo");
        } else {
            /*
             * log.info("[~] Seteando condiciones --  size " + condiciones.size());
             * for (Condicion item : condiciones) {
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

    public static class Builder implements QoopoBuilder<FilterController> {

        private final FilterController gestor = new FilterController();

        public Builder() {
            //
        }

        public Builder filtro(Filter filtro) {
            gestor.setFiltro(filtro);
            return this;
        }

        public Builder accion(Accion accion) {
            gestor.setAccion(accion);
            return this;
        }

        public Builder agregarCondicionDisponible(Condicion condicion) {
            gestor.agregarCondicionDisponible(condicion);
            return this;
        }

        public Builder condicionPermanente(Condicion condicionPermanente) {
            gestor.condicionPermanente = condicionPermanente;
            return this;
        }

        public Builder campos(List<Campo> campos) {
            gestor.setCamposDisponibles(campos);
            return this;
        }

        @Override
        public FilterController build() {
            return gestor;
        }

    }

}
