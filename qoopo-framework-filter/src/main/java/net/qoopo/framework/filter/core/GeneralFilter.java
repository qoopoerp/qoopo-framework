package net.qoopo.framework.filter.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.qoopo.framework.date.FechasUtil;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.ConditionCollection;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.filter.core.condition.Function;
import net.qoopo.framework.filter.core.condition.Value;

public class GeneralFilter {

    public static final Field ARCHIVADO = new Field(Field.BOLEANO, "Archivado", "o.archived");

    public static Condition condicionActivo() {
        return Condition.getOpen().concat(
                Condition.builder().field(GeneralFilter.ARCHIVADO).function(Function.IGUAL)
                        .value(new Value("archived", Boolean.FALSE)).build()
                        .or(Condition.builder().field(GeneralFilter.ARCHIVADO).function(Function.ES_NULO).build())
                        .close())
                .setName("Activo");
    }

    public static Condition condicionArchivado() {
        return Condition.builder().field(GeneralFilter.ARCHIVADO).function(Function.IGUAL)
                .value(new Value("archived", Boolean.TRUE)).build().setName("Archivado");
    }

    /**
     * Filtros de fecha
     */
    public static ConditionCollection dateField(Field field) {
        return ConditionCollection.of(field.getNombre(),
                List.of(
                        GeneralFilter.fechaHoy(field),
                        ConditionCollection.of("Mes",
                                List.of(GeneralFilter.fechaEsteMes(field),
                                        GeneralFilter.fechaMesAnterior(field),
                                        monthsOfYear(String.valueOf(LocalDate.now().getYear()), field, LocalDate.now()),
                                        monthsOfYear(String.valueOf(LocalDate.now().plusYears(-1).getYear()), field,
                                                LocalDate.now().plusYears(-1)))),
                        ConditionCollection.of("Año",
                                List.of(
                                        GeneralFilter.year(field, LocalDate.now()),
                                        GeneralFilter.year(field, LocalDate.now().plusYears(-1)),
                                        GeneralFilter.year(field, LocalDate.now().plusYears(-2))))));
    }

    /**
     * Devuelve una coleccion de meses para el año de la fecha indicada
     * 
     * @param field
     * @param date
     * @return
     */
    public static ConditionCollection monthsOfYear(String name, Field field, LocalDate date) {
        List<Condition> lstMonts = new ArrayList<>();
        LocalDate startDate = FechasUtil.fechaInicioAnio(date).toLocalDate();
        for (int i = 1; i <= 12; i++) {
            LocalDate endDate = FechasUtil
                    .convertToLocalDateViaInstant(FechasUtil.fechaFinMes(FechasUtil.convertLocalDate(startDate)));
            lstMonts.add(dateRange(field, startDate, endDate)
                    .setName(startDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US)));
            startDate = startDate.plusMonths(1);
        }
        return ConditionCollection.of(name, lstMonts);
    }

    public static Condition dateRange(Field field, LocalDate startDate, LocalDate endDate) {
        return Condition.builder().name(field.getNombre() + ": Range").field(field).function(Function.ENTRE)
                .value(new Value(FechasUtil.fechaInicio(startDate)))
                .value2(new Value(FechasUtil.fechaFin(endDate))).build();
    }

    public static Condition dateRange(Field field, LocalDateTime startDate, LocalDateTime endDate) {
        return Condition.builder().name(field.getNombre() + ": Range").field(field).function(Function.ENTRE)
                .value(new Value(FechasUtil.fechaInicio(startDate)))
                .value2(new Value(FechasUtil.fechaFin(endDate))).build();
    }

    public static Condition fechaHoy(Field field) {
        return dateRange(field, FechasUtil.fechaInicio(LocalDateTime.now()), FechasUtil.fechaFin(LocalDateTime.now()))
                .setName("Hoy");

    }

    public static Condition month(Field field, LocalDate date) {
        return dateRange(field, FechasUtil.fechaInicioMes(LocalDateTime.now()),
                FechasUtil.fechaFinMes(LocalDateTime.now()))
                .setName(
                        String.valueOf(date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())));
    }

    public static Condition fechaEsteMes(Field field) {
        return dateRange(field, FechasUtil.fechaInicioMes(LocalDateTime.now()),
                FechasUtil.fechaFinMes(LocalDateTime.now())).setName("Mes Actual");
    }

    public static Condition fechaMesAnterior(Field field) {
        return dateRange(field, FechasUtil.fechaInicioMes(LocalDateTime.now().plusMonths(-1)),
                FechasUtil.fechaFinMes(LocalDateTime.now().plusMonths(-1))).setName("Mes anterior");
    }

    public static Condition year(Field field, LocalDate date) {
        return dateRange(field, FechasUtil.fechaInicioAnio(date),
                FechasUtil.fechaFinAnio(date)).setName(String.valueOf(date.getYear()));
    }

    /**
     * Lista todos los registros
     * 
     * @return
     */
    public static Filter all(String TABLA_JPL) {
        Filter filtro = new Filter();
        filtro.setName("Todos");
        filtro.setCollection(TABLA_JPL);
        filtro.setNext("order by o.order");
        filtro.setOrderDirection("asc");
        return filtro;
    }

}
