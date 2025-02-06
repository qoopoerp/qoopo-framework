package net.qoopo.framework.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class FechasUtil {
    public static final int DIAS = 1;

    public static final int MESES = 2;

    public static final int ANIOS = 3;

    public static final int HORAS = 4;

    public static final int MINUTOS = 5;

    public static final int SEGUNDOS = 6;

    public static final int SEMANAS = 7;

    public static int getAnioActual() {
        return getParteFecha(new Date(), 3);
    }

    public static int getAnioAnterior() {
        return getParteFecha(sumarAnios(new Date(), -1), 3);
    }

    public static int getParteFecha(Date fecha, int tipo) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        switch (tipo) {
            case MESES:
                return c.get(Calendar.MONTH) + 1;
            case ANIOS:
                return c.get(Calendar.YEAR);
            case SEMANAS:
                return c.get(Calendar.WEEK_OF_MONTH);
            case HORAS:
                return c.get(Calendar.HOUR_OF_DAY);
            case MINUTOS:
                return c.get(Calendar.MINUTE);
            case SEGUNDOS:
                return c.get(Calendar.SECOND);
        }
        return c.get(5);
    }

    public static Date getHora(int hora, int minuto, int segundo, TimeZone tz) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(tz);
        c.set(11, hora);
        c.set(12, minuto);
        c.set(13, segundo);
        return c.getTime();
    }

    public static Date fechaInicio(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return c.getTime();
    }

    public static Date fechaFin(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        return c.getTime();
    }

    public static LocalDateTime fechaInicio(LocalDateTime fecha) {
        return LocalDateTime.of(fecha.getYear(), fecha.getMonth(), fecha.getDayOfMonth(), 0, 0, 0);
    }

    public static LocalDateTime fechaInicio(LocalDate fecha) {
        return LocalDateTime.of(fecha.getYear(), fecha.getMonth(), fecha.getDayOfMonth(), 0, 0, 0);
    }

    public static LocalDateTime fechaFin(LocalDateTime fecha) {
        return LocalDateTime.of(fecha.getYear(), fecha.getMonth(), fecha.getDayOfMonth(), 23, 59, 59);
    }

    public static LocalDateTime fechaFin(LocalDate fecha) {
        return LocalDateTime.of(fecha.getYear(), fecha.getMonth(), fecha.getDayOfMonth(), 23, 59, 59);
    }

    public static Date fechaInicio(int anio, int mes) {
        Calendar c = Calendar.getInstance();
        c.set(1, anio);
        c.set(2, mes - 1);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return c.getTime();
    }

    public static Date fechaFin(int anio, int mes) {
        Calendar c = Calendar.getInstance();
        c.set(1, anio);
        c.set(2, mes - 1);
        c.set(5, c.getActualMaximum(5));
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        return c.getTime();
    }

    public static LocalDateTime fechaInicioLDT(int anio, int mes) {
        return LocalDateTime.of(anio, mes, 1, 0, 0, 0);
    }

    public static LocalDateTime fechaFinLDT(int anio, int mes) {
        return LocalDateTime.of(anio, mes, YearMonth.of(anio, mes).lengthOfMonth(), 23, 59, 59);
    }

    public static Date fechaInicioMesActual() {
        return fechaInicioMes(new Date());
    }

    public static Date fechaFinMesActual() {
        return fechaFinMes(new Date());
    }

    public static Date fechaInicioAnioActual() {
        return fechaInicioAnio(new Date());
    }

    public static Date fechaFinAnioActual() {
        return fechaFinAnio(new Date());
    }

    public static Date fechaInicioMesAnterior() {
        return fechaInicioMes(sumarMeses(new Date(), -1));
    }

    public static Date fechaFinMesAnterior() {
        return fechaFinMes(sumarMeses(new Date(), -1));
    }

    public static Date fechaInicioMes(Date fecha) {
        return fechaInicio(getParteFecha(fecha, 3), getParteFecha(fecha, 2));
    }

    public static Date fechaFinMes(Date fecha) {
        return fechaFin(getParteFecha(fecha, 3), getParteFecha(fecha, 2));
    }

    public static Date fechaInicioAnio(Date fecha) {
        return fechaInicio(getParteFecha(fecha, 3), 1);
    }

    public static Date fechaFinAnio(Date fecha) {
        return fechaFin(getParteFecha(fecha, 3), 12);
    }

    public static LocalDateTime fechaInicioMesActualLDT() {
        return fechaInicioMes(LocalDateTime.now());
    }

    public static LocalDateTime fechaFinMesActualLDT() {
        return fechaFinMes(LocalDateTime.now());
    }

    public static LocalDateTime fechaInicioAnioActualLDT() {
        return fechaInicioAnio(LocalDateTime.now());
    }

    public static LocalDateTime fechaFinAnioActualLDT() {
        return fechaFinAnio(LocalDateTime.now());
    }

    public static LocalDateTime fechaInicioMesAnteriorLDT() {
        return fechaInicioMes(LocalDateTime.now().plusMonths(-1L));
    }

    public static LocalDateTime fechaFinMesAnteriorLDT() {
        return fechaFinMes(LocalDateTime.now().plusMonths(-1L));
    }

    public static LocalDateTime fechaInicioMes(LocalDateTime fecha) {
        return fechaInicioLDT(fecha.getYear(), fecha.getMonthValue());
    }

    public static LocalDateTime fechaFinMes(LocalDateTime fecha) {
        return fechaFinLDT(fecha.getYear(), fecha.getMonthValue());
    }

    public static LocalDateTime fechaInicioAnio(LocalDateTime fecha) {
        return fechaInicioLDT(fecha.getYear(), 1);
    }

    public static LocalDateTime fechaFinAnio(LocalDateTime fecha) {
        return fechaFinLDT(fecha.getYear(), 12);
    }

    public static LocalDateTime fechaInicioTrimestre(LocalDateTime fecha) {
        LocalDateTime fechaInicio = fechaInicioLDT(fecha.getYear(), 1);
        LocalDateTime fechaFin = fechaFinLDT(fecha.getYear(), 3);
        for (int i = 0; i < 4; i++) {
            fechaInicio = fechaInicio.plusMonths((i * 3));
            fechaFin = fechaFin.plusMonths((i * 3));
            if ((fecha.isAfter(fechaInicio) || fecha.isEqual(fechaInicio)) && (fecha
                    .isBefore(fechaFin) || fecha.isEqual(fechaFin)))
                return fechaInicio;
        }
        return fechaInicio;
    }

    public static LocalDateTime fechaFinTrimestre(LocalDateTime fecha) {
        LocalDateTime fechaInicio = fechaInicioLDT(fecha.getYear(), 1);
        LocalDateTime fechaFin = fechaFinLDT(fecha.getYear(), 3);
        for (int i = 0; i < 4; i++) {
            fechaInicio = fechaInicio.plusMonths((i * 3));
            fechaFin = fechaFin.plusMonths((i * 3));
            if ((fecha.isAfter(fechaInicio) || fecha.isEqual(fechaInicio)) && (fecha
                    .isBefore(fechaFin) || fecha.isEqual(fechaFin)))
                return fechaFin;
        }
        return fechaFin;
    }

    public static LocalDateTime fechaInicioAnio(LocalDate fecha) {
        return fechaInicioLDT(fecha.getYear(), 1);
    }

    public static LocalDateTime fechaFinAnio(LocalDate fecha) {
        return fechaFinLDT(fecha.getYear(), 12);
    }

    public static LocalDateTime fechaInicioTrimestre(LocalDate fecha) {
        LocalDateTime fechaInicio = fechaInicioLDT(fecha.getYear(), 1);
        LocalDateTime fechaFin = fechaFinLDT(fecha.getYear(), 3);
        for (int i = 0; i < 4; i++) {
            fechaInicio = fechaInicio.plusMonths((i * 3));
            fechaFin = fechaFin.plusMonths((i * 3));
            if ((fecha.isAfter(fechaInicio.toLocalDate()) || fecha.isEqual(fechaInicio.toLocalDate())) && (fecha
                    .isBefore(fechaFin.toLocalDate()) || fecha.isEqual(fechaFin.toLocalDate())))
                return fechaInicio;
        }
        return fechaInicio;
    }

    public static LocalDateTime fechaFinTrimestre(LocalDate fecha) {
        LocalDateTime fechaInicio = fechaInicioLDT(fecha.getYear(), 1);
        LocalDateTime fechaFin = fechaFinLDT(fecha.getYear(), 3);
        for (int i = 0; i < 4; i++) {
            fechaInicio = fechaInicio.plusMonths((i * 3));
            fechaFin = fechaFin.plusMonths((i * 3));
            if ((fecha.isAfter(fechaInicio.toLocalDate()) || fecha.isEqual(fechaInicio.toLocalDate())) && (fecha
                    .isBefore(fechaFin.toLocalDate()) || fecha.isEqual(fechaFin.toLocalDate())))
                return fechaFin;
        }
        return fechaFin;
    }

    public static Date sumarMinutos(Date fecha, int minutos) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(12, minutos);
        return c.getTime();
    }

    public static Date sumarHora(Date fecha, int horas) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(11, horas);
        return c.getTime();
    }

    public static Date sumarDia(Date fecha, int dias) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(5, dias);
        return c.getTime();
    }

    public static Date sumarMeses(Date fecha, int meses) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(2, meses);
        return c.getTime();
    }

    public static Date sumarAnios(Date fecha, int anios) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(1, anios);
        return c.getTime();
    }

    public static long diferencia(Date fechaInicial, Date fechaFinal, int tipo) {
        long diferenciaMilisegundos = fechaFinal.getTime() - fechaInicial.getTime();
        switch (tipo) {
            case 3:
                return diferenciaMilisegundos / 31536000000L;
            case 2:
                return diferenciaMilisegundos / 2592000000L;
            case 7:
                return diferenciaMilisegundos / 604800000L;
            case 4:
                return diferenciaMilisegundos / 3600000L;
            case 5:
                return diferenciaMilisegundos / 60000L;
            case 6:
                return diferenciaMilisegundos / 1000L;
        }
        return diferenciaMilisegundos / 86400000L;
    }

    public static long diferencia(LocalDateTime fechaInicial, LocalDateTime fechaFinal, int tipo) {
        switch (tipo) {
            case 3:
                return fechaInicial.until(fechaFinal, ChronoUnit.YEARS);
            case 2:
                return fechaInicial.until(fechaFinal, ChronoUnit.MONTHS);
            case 7:
                return fechaInicial.until(fechaFinal, ChronoUnit.WEEKS);
            case 4:
                return fechaInicial.until(fechaFinal, ChronoUnit.HOURS);
            case 5:
                return fechaInicial.until(fechaFinal, ChronoUnit.MINUTES);
            case 6:
                return fechaInicial.until(fechaFinal, ChronoUnit.SECONDS);
        }
        return fechaInicial.until(fechaFinal, ChronoUnit.DAYS);
    }

    public static XMLGregorianCalendar convertirXMLGregorianCalendar(Date fecha) {
        try {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(fecha);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(FechasUtil.class.getName()).log(Level.SEVERE, (String) null, ex);
            return null;
        }
    }

    public static XMLGregorianCalendar convertirXMLGregorianCalendar(LocalDateTime fecha) {
        return convertirXMLGregorianCalendar(convertLocalDate(fecha));
    }

    public static Date convertLocalDate(LocalDate localDate) {
        if (localDate == null)
            return null;
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        if (dateToConvert == null)
            return null;
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        if (dateToConvert == null)
            return null;
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        if (dateToConvert == null)
            return null;
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        if (dateToConvert == null)
            return null;
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date mayor(Date fecha1, Date fecha2) {
        return fecha1.after(fecha2) ? fecha1 : fecha2;
    }

    public static Date menor(Date fecha1, Date fecha2) {
        return fecha1.before(fecha2) ? fecha1 : fecha2;
    }

    public static LocalDateTime mayor(LocalDateTime fecha1, LocalDateTime fecha2) {
        return fecha1.isAfter(fecha2) ? fecha1 : fecha2;
    }

    public static LocalDateTime menor(LocalDateTime fecha1, LocalDateTime fecha2) {
        return fecha1.isBefore(fecha2) ? fecha1 : fecha2;
    }
}
