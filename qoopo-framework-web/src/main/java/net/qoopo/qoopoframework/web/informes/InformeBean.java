package net.qoopo.qoopoframework.web.informes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.qoopoframework.lang.LanguageProvider;
import net.qoopo.qoopoframework.util.PeriodoManager;
import net.qoopo.qoopoframework.web.AppSessionBeanInterface;
import net.qoopo.qoopoframework.web.GestorFecha;
import net.qoopo.qoopoframework.web.vistas.ReporteBean;
import net.qoopo.util.date.FechasUtil;

/**
 * Funcionamiento básico para los beans que generan informes
 */
@Getter
@Setter
public abstract class InformeBean implements Serializable {

    public static final Logger log = Logger.getLogger("Qoopo");

    protected PeriodoManager periodoManager;

    protected int tipoCarga = 1;

    @Inject
    protected AppSessionBeanInterface sessionBean;

    @Inject
    protected GestorFecha gestorFecha;

    @Inject
    protected ReporteBean reporteBean;

    @Inject
    protected LanguageProvider languageProvider;

    protected int actual;

    protected boolean comparar = false;
    protected boolean verVariacion = false;

    protected List<String> optionsLabels = new ArrayList<>();

    protected int tipoReporte = 1;
    protected List<SelectItem> lstReportes = new ArrayList<>();

    protected int optionValue1 = 0;
    protected List<SelectItem> lstOptions1 = new ArrayList<>();

    protected int optionValue2 = 0;
    protected List<SelectItem> lstOptions2 = new ArrayList<>();

    protected int optionValue3 = 0;
    protected List<SelectItem> lstOptions3 = new ArrayList<>();

    protected int optionValue4 = 0;
    protected List<SelectItem> lstOptions4 = new ArrayList<>();

    public InformeBean() {
        periodoManager = new PeriodoManager();
    }

    public void selectToday() {
        log.info("[+] Selecionado Hoy");
        periodoManager.setFechaInicio(FechasUtil.fechaInicioAnioActualLDT().toLocalDate());
        periodoManager.setFecha(LocalDate.now());
        periodoManager.setTipoPeriodo(PeriodoManager.PERIODO_TIPO_ANUAL);
    }

    public void selectThisMonth() {
        log.info("[+] Selecionado Este mes");
        periodoManager.setFechaInicio(FechasUtil.fechaInicioMes(LocalDateTime.now()).toLocalDate());
        periodoManager.setFecha(FechasUtil.fechaFinMes(LocalDateTime.now()).toLocalDate());
        periodoManager.setTipoPeriodo(PeriodoManager.PERIODO_TIPO_ANUAL);
    }

    public void selectThisQuarter() {
        log.info("[+] Selecionado este trimestre");
        periodoManager.setFechaInicio(FechasUtil.fechaInicioTrimestre(LocalDateTime.now()).toLocalDate());
        periodoManager.setFecha(FechasUtil.fechaFinTrimestre(LocalDateTime.now()).toLocalDate());
        periodoManager.setTipoPeriodo(PeriodoManager.PERIODO_TIPO_TRIMESTRAL);
    }

    public void selectThisYear() {
        log.info("[+] Selecionado Este anio");
        periodoManager.setFechaInicio(FechasUtil.fechaInicioAnio(LocalDateTime.now()).toLocalDate());
        periodoManager.setFecha(FechasUtil.fechaFinAnio(LocalDateTime.now()).toLocalDate());
        periodoManager.setTipoPeriodo(PeriodoManager.PERIODO_TIPO_ANUAL);
    }

    public void selectLastMonth() {
        log.info("[+] Selecionado mes anterior");
        periodoManager.setFechaInicio(FechasUtil.fechaInicioMes(LocalDateTime.now().minusMonths(1)).toLocalDate());
        periodoManager.setFecha(FechasUtil.fechaFinMes(LocalDateTime.now().minusMonths(1)).toLocalDate());
        periodoManager.setTipoPeriodo(PeriodoManager.PERIODO_TIPO_MENSUAL);
    }

    public void selectLastQuarter() {
        log.info("[+] Selecionado trimestre anterior");
        periodoManager
                .setFechaInicio(FechasUtil.fechaInicioTrimestre(LocalDateTime.now().minusMonths(3)).toLocalDate());
        periodoManager.setFecha(FechasUtil.fechaFinTrimestre(LocalDateTime.now().minusMonths(3)).toLocalDate());
        periodoManager.setTipoPeriodo(PeriodoManager.PERIODO_TIPO_TRIMESTRAL);
    }

    public void selectLastYear() {
        log.info("[+] Selecionado anio anterior");
        periodoManager.setFechaInicio(FechasUtil.fechaInicioAnio(LocalDateTime.now().minusYears(1)).toLocalDate());
        periodoManager.setFecha(FechasUtil.fechaFinAnio(LocalDateTime.now().minusYears(1)).toLocalDate());
        periodoManager.setTipoPeriodo(PeriodoManager.PERIODO_TIPO_ANUAL);
    }

}
