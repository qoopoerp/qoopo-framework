package net.qoopo.qoopoframework.web.vistas;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import net.qoopo.qoopoframework.web.core.Widget;
import net.qoopo.util.date.FechasUtil;
import net.qoopo.util.graph.QChartJS;

public abstract class GraphWidget extends Widget {
    public void setChart(QChartJS chart) {
        this.chart = chart;
    }

    public void setOpcionGrafico(int opcionGrafico) {
        this.opcionGrafico = opcionGrafico;
    }

    public void setMaxMesesGraficos(int maxMesesGraficos) {
        this.maxMesesGraficos = maxMesesGraficos;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public static final Logger log = Logger.getLogger("Qoopo");

    protected final transient SimpleDateFormat sdf = new SimpleDateFormat("yy/MM");

    public SimpleDateFormat getSdf() {
        return this.sdf;
    }

    protected final transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM");

    protected QChartJS chart;

    public static final int GRAPH_BAR = 1;
    public static final int GRAPH_AREA = 2;
    public static final int GRAPH_PIE = 3;
    public static final int GRAPH_BARLINE = 4;
    public static final int GRAPH_BAR_HORIZONTAL = 5;

    public DateTimeFormatter getDtf() {
        return this.dtf;
    }

    public QChartJS getChart() {
        return this.chart;
    }

    protected int opcionGrafico = 1;

    public int getOpcionGrafico() {
        return this.opcionGrafico;
    }

    protected int maxMesesGraficos = 5;

    protected String title;

    public int getMaxMesesGraficos() {
        return this.maxMesesGraficos;
    }

    public String getTitle() {
        return this.title;
    }

    protected boolean stacked = false;

    public boolean isStacked() {
        return this.stacked;
    }

    protected String getGrupoGrafico(LocalDateTime fecha) {
        return getGrupoGrafico(FechasUtil.fechaInicioMes(fecha), FechasUtil.fechaFinMes(LocalDateTime.now()), null,
                FechasUtil.MESES,
                this.maxMesesGraficos, ">", "<", -this.maxMesesGraficos, this.maxMesesGraficos);
    }

    protected String getGrupoGrafico(LocalDateTime fecha, LocalDateTime fechaCriterio) {
        return getGrupoGrafico(FechasUtil.fechaInicioMes(fecha), FechasUtil.fechaInicioMes(fechaCriterio), null,
                FechasUtil.MESES,
                this.maxMesesGraficos, ">", "<", -this.maxMesesGraficos, this.maxMesesGraficos);
    }

    protected String getGrupoGrafico(LocalDateTime fecha, LocalDateTime fechaCriterio, List<String> labels,
            int tipoPeriodo, int maxPeriodos, String maxLabel, String minLabel, int minLimit, int maxLimit) {
        long diferenciaPeriodos = 0L;
        try {
            if (fecha == null)
                return "N/A";
            diferenciaPeriodos = FechasUtil.diferencia(fechaCriterio, fecha, tipoPeriodo);
            if (diferenciaPeriodos > maxLimit)
                return maxLabel;
            if (diferenciaPeriodos > minLimit && diferenciaPeriodos <= maxLimit) {
                if (labels == null || labels.isEmpty() || labels.size() < maxPeriodos)
                    return fecha.format(this.dtf);
                return labels.get(Math.max((int) diferenciaPeriodos + Math.abs(minLimit), 0));
            }
            return minLabel;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("voy a devolver N/Am diferencia periodos=" + diferenciaPeriodos);
            return "N/A";
        }
    }

    public void finalizar() {
        try {
            this.chart.setStacked(this.stacked);
            this.chart.setMostrarLineas(false);
            this.chart.groupSum();
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
                    this.chart.createBarLineModel();
                    break;
            }
        } catch (Exception exception) {
        }
    }

    public void modeBar() {
        this.opcionGrafico = 1;
    }

    public void modeArea() {
        this.opcionGrafico = 2;
    }

    public void modePie() {
        this.opcionGrafico = 3;
    }

    public void updateOpcionGrafico(int opcionGrafico) {
        this.opcionGrafico = opcionGrafico;
        cargar();
    }

    public abstract void cargar();
}
