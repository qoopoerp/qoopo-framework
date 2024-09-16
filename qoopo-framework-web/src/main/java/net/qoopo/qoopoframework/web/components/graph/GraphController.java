package net.qoopo.qoopoframework.web.components.graph;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.formula.functions.T;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.qoopoframework.jpa.core.EntidadBase;
import net.qoopo.qoopoframework.jpa.core.interfaces.Graficable;
import net.qoopo.qoopoframework.lang.LanguageProvider;
import net.qoopo.qoopoframework.models.OpcionBase;
import net.qoopo.qoopoframework.web.core.jpa.AdminAbstractClass;
import net.qoopo.util.NVL;
import net.qoopo.util.graph.QChartJS;

/**
 * Este controller maneja la vista de Graficos de todos los modulos
 *
 * @author ALBERTO
 */
@Getter
@Setter
public class GraphController<T extends EntidadBase> implements Serializable {

    public static final Logger log = Logger.getLogger("Qoopo-graph-controller");

    protected LanguageProvider languageProvider;

    protected QChartJS chart = new QChartJS();
    protected OpcionBase opcionGrupo = new OpcionBase(-100, 0, "'");
    protected OpcionBase opcionGrupo2 = new OpcionBase(-100, 0, "'");
    protected OpcionBase opcionMetrica = new OpcionBase(-100, 0, "'");
    protected List<OpcionBase> opcionesGrupo = new ArrayList<>();
    protected List<OpcionBase> opcionesMetrica = new ArrayList<>();
    private Iterable<T> datos;
    private int opcionGrafico = 1; // 1 barra, 2 area, 3 circular
    private boolean metricasCargadas = false;
    private int tipoOrden = QChartJS.ORDER_NOORDER;
    private boolean stacked = true;
    private boolean leyenda = true;
    private boolean mostrarTitulo = false;
    private boolean acumular = false;

    public static final int GRAPH_BAR = 1;
    public static final int GRAPH_AREA = 2;
    public static final int GRAPH_PIE = 3;
    public static final int GRAPH_BARLINE = 4;
    public static final int GRAPH_BAR_HORIZONTAL = 5;

    public GraphController(LanguageProvider textoBean) {
        // vacio
        chart.addElement("valor", 0);
        chart.setMaxLengthLabels(35);
        chart.groupSum();
        chart.createBarModel();
        this.languageProvider = textoBean;
    }

    public void cargarDatos(Object bean) {
        try {
            if (bean != null) {
                if (bean instanceof AdminAbstractClass) {
                    AdminAbstractClass<T> mBean = (AdminAbstractClass<T>) bean;
                    setDatos(mBean.getData());
                    recargar();
                }
            }
        } catch (Exception e) {
            //
        }
    }

    protected void cargarMetricas(Object entidad) {
        if (!metricasCargadas) {
            opcionesGrupo.clear();
            opcionesMetrica.clear();
            if (entidad instanceof Graficable) {
                Graficable g = (Graficable) entidad;
                if (g.getOpcionesGrupos() != null) {
                    opcionesGrupo.addAll(g.getOpcionesGrupos());
                }
                if (g.getOpcionesMetricas() != null) {
                    opcionesMetrica.addAll(g.getOpcionesMetricas());
                }
                metricasCargadas = true;
            }
        }
    }

    /**
     * como en el select item que hay en la pantalla soloa ctualiza el codigo
     * (no hay converter), por lo tanto el codigo texto siempre seria 0, asi que
     * en este codigo busco el codigo del texto en la lista de las opciones, en
     * funcion codigo del codigo seleccionado
     */
    private void actualizarOpcionMetrica() {
        try {
            for (OpcionBase opcion : opcionesMetrica) {
                if (opcion.getCodigo() == opcionMetrica.getCodigo()) {
                    opcionMetrica.setCodigoTexto(opcion.getCodigoTexto());
                    opcionMetrica.setTexto(opcion.getTexto());
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void clear() {
        try {
            chart.clear();
        } catch (Exception e) {

        }
    }

    /**
     * Recarga el grafico
     */
    public void recargar() {
        try {
            log.info("[+] graph controller loading...");
            metricasCargadas = false;
            chart.clear();
            chart.setTipoOrden(tipoOrden);
            chart.setStacked(stacked);
            chart.setMostrarLeyenda(leyenda);
            chart.setMostrarTitulo(mostrarTitulo);
            chart.setAcumulativa(acumular);

            actualizarOpcionMetrica();
            if (datos != null) {
                datos.forEach(t -> {
                    cargarMetricas(t);
                    if (t instanceof Graficable) {
                        Graficable g = (Graficable) t;
                        // Cuando solo se selecciona un grupo o el segundo grupo es el mismo que el
                        // primero
                        if (opcionGrupo2.getCodigo() == -100 || (opcionGrupo2.getCodigo() == opcionGrupo.getCodigo())) {
                            for (String itemGrupo : g.getGrupo(opcionGrupo)) {
                                // en este caso como nombre de la seria no usamos el grupo sino el nombre de la
                                // metrica
                                chart.addElement(languageProvider.getTextValue(opcionMetrica.getCodigoTexto()),
                                        itemGrupo,
                                        (BigDecimal) NVL.nvl(
                                                g.getGrupoValor(opcionMetrica, opcionGrupo, itemGrupo, null, null)));
                            }
                        } else {
                            for (String itemGrupo : g.getGrupo(opcionGrupo)) {
                                for (String itemGrupo2 : g.getGrupo(opcionGrupo2)) {
                                    // usamos el grupo 2 como nombre de la serie, y como leyeda el grupo 1

                                    // En este escenario tenemos un problema. El valor que se debe enviar para la
                                    // suma no se hace de manera correcta.
                                    // Ejemplo: Si existe una factura que tiene dos productos, del Cliente JOSE
                                    // PEREZ, el producto A con 100 y el producto B con 50
                                    // Si se agrupa primero por Cliente y luego por Producto deberia salir una
                                    // columna del cliente JOSE PEREZ con un dos subgrupos uno por cada producto y
                                    // un total de 150
                                    // Si se agrupa primero por Producto y luego contacto, deberia salir dos
                                    // columnas cada uno por el producto, y omo subgrupo el cliente, con el valor
                                    // del producto

                                    // Opción 1 - enviar el valor del grupo 1
                                    // --> En caso de agrupar por producto y luego por Cliente, el valor total es
                                    // correcto pues solo se toma el valor dle producto
                                    // -- > En caso de agrupar por Cliente y luego por producto, el valor es
                                    // incorrecto, pues al haber dos subgrupos (uno por cada producto) a cada
                                    // subgrupo le va a sumar el valor del cliente y no del producto

                                    // Opción 2 - enviar el valor del grupo 2
                                    // Aca funcionalira al reves

                                    // Al ser solo un chart, lo correcto es enviar el valor que corresponde al
                                    // dataet, en este caso al grupo 2, lo que esta mal es el valor obtenido,
                                    // pues debe ser calculado en funcion de los dos grupos y no solo de uno
                                    // pues deberia ser el valor que coresponde al grupo2 y a la vez al grupo 1
                                    if (opcionGrupo.getNivel() <= opcionGrupo2.getNivel()) {
                                        chart.addElement(itemGrupo2, itemGrupo,
                                                (BigDecimal) NVL
                                                        .nvl(g.getGrupoValor(opcionMetrica, opcionGrupo2, itemGrupo2,
                                                                opcionGrupo, itemGrupo)));
                                    } else {
                                        chart.addElement(itemGrupo2, itemGrupo,
                                                (BigDecimal) NVL
                                                        .nvl(g.getGrupoValor(opcionMetrica, opcionGrupo, itemGrupo,
                                                                opcionGrupo2, itemGrupo2)));
                                    }
                                }
                            }
                        }
                    }
                });

                chart.groupSum();
                switch (this.opcionGrafico) {
                    case GRAPH_BAR:
                    case GRAPH_BAR_HORIZONTAL:
                        this.chart.createBarModel();
                        break;
                    case GRAPH_AREA:
                        this.chart.crearModeloArea();
                        break;
                    case GRAPH_PIE:
                        this.chart.crearModeloPie();
                        break;
                    case GRAPH_BARLINE:
                        this.chart.createBarLineModel(false);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                chart.addElement("value", 0, "initial", 0);
                chart.groupSum();
                // chart.createBarModel();
                chart.createBarLineModel(false);
                e.printStackTrace();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            log.log(Level.SEVERE, "", e);
        }
    }

    public void updateOpcionGrafico(int opcion) {
        this.opcionGrafico = opcion;
        recargar();
    }

    public void updateOrderType(int opcion) {
        this.tipoOrden = opcion;
        recargar();
    }

    public String getIconSort() {
        switch (tipoOrden) {
            case 1:
                return "pi-sort-amount-up";
            case 2:
                return "pi-sort-amount-down-alt";
            default:
                return "pi-sort-alt-slash";
        }
    }

    public String getIconChart() {
        switch (opcionGrafico) {
            case 2:
                return "pi-chart-line";
            case 3:
                return "pi-chart-pie";
            case 5:
                return "pi-align-left";
            default:
                return "pi-chart-bar";
        }
    }
}
