package net.qoopo.framework.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Utilidad para el uso de periodos en la generación de informes
 * 
 * Un periodo es una porción de tiempo en el cual se separan los datos a
 * analizar,
 * por ejemplo en un informe contable se puede comparar los valores contables de
 * un mes a otro, siendo el mes un periodo
 * 
 * Lo valores para los periodos son dias, meses semestres y años
 */
@Getter
@Setter
public class PeriodoManager {

    public static final int PERIODO_TIPO_MENSUAL = 1;
    public static final int PERIODO_TIPO_TRIMESTRAL = 2;
    public static final int PERIODO_TIPO_SEMESTRAL = 3;
    public static final int PERIODO_TIPO_ANUAL = 4;

    // el numero de periodos hacia atras para comparar
    protected int periodos = 2;
    protected int tipoPeriodo = 1;
    protected LocalDate fechaInicio; // a la fecha que se genera el reporte
    protected LocalDate fecha; // a la fecha que se genera el reporte
    protected List<LocalDate> fechasInicios = new ArrayList<>();
    protected List<LocalDate> fechasFin = new ArrayList<>();
    protected List<Integer> lstPeriodos = new ArrayList<>();

    public PeriodoManager() {
        fecha = LocalDate.now();
    }

    public LocalDate getFecha(int periodo) {
        switch (periodo) {
            case 0:
                return fecha;

            default:
                if (periodo < fechasFin.size())
                    return fechasFin.get(periodo);
                else
                    return fecha;
        }
    }

    public void setFecha(int periodo, LocalDate value) {
        switch (periodo) {
            case 0:
                setFecha(value);
                break;

            default:
                if (periodo < fechasFin.size())
                    fechasFin.set(periodo, value);
                else
                    setFecha(value);
        }
    }

    public LocalDate getFechaInicio(int periodo) {
        switch (periodo) {
            case 0:
                return fechaInicio;
            default:
                if (periodo < fechasInicios.size())
                    return fechasInicios.get(periodo);
                else
                    return fechaInicio;
        }
    }

    public void setFechaInicio(int periodo, LocalDate value) {
        switch (periodo) {
            case 0:
                setFechaInicio(value);
                break;

            default:
                if (periodo < fechasInicios.size())
                    fechasInicios.set(periodo, value);
                else
                    setFechaInicio(value);
        }
    }

    public void config(boolean enable) {
        // this.comparar = filtroInformesContables.isComparar();
        // this.periodos = filtroInformesContables.getPeriodos();
        // this.tipoPeriodo = filtroInformesContables.getTipoFiltro();

        if (fecha == null) {
            fecha = LocalDate.now();
        }

        if (!enable) {
            periodos = 1;
        }
        periodos = Math.min(periodos, 4);

        lstPeriodos.clear();
        fechasInicios.clear();
        fechasFin.clear();
        for (int i = 0; i < periodos; i++) {
            // setFecha(i, fecha.minusYears(i));
            if (fechaInicio != null) {
                switch (tipoPeriodo) {
                    case PERIODO_TIPO_ANUAL:
                        fechasInicios.add(fechaInicio.minusYears(i));
                        break;
                    case PERIODO_TIPO_MENSUAL:
                        fechasInicios.add(fechaInicio.minusMonths(i));
                        break;

                    case PERIODO_TIPO_TRIMESTRAL:
                        fechasInicios.add(fechaInicio.minusMonths(i * 3));
                        break;
                    case PERIODO_TIPO_SEMESTRAL:
                        fechasInicios.add(fechaInicio.minusMonths(i * 6));
                        break;
                }
            }

            if (fecha != null) {
                switch (tipoPeriodo) {
                    case PERIODO_TIPO_ANUAL:
                        fechasFin.add(fecha.minusYears(i));
                        break;
                    case PERIODO_TIPO_MENSUAL:
                        fechasFin.add(fecha.minusMonths(i));
                        break;
                    case PERIODO_TIPO_TRIMESTRAL:
                        fechasFin.add(fecha.minusMonths(i * 3));
                        break;
                    case PERIODO_TIPO_SEMESTRAL:
                        fechasFin.add(fecha.minusMonths(i * 6));
                        break;
                }
            }
            lstPeriodos.add(i);
        }
    }

}
