package net.qoopo.framework.filter.core;

import java.time.LocalDateTime;

import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Function;
import net.qoopo.framework.filter.core.condition.Value;
import net.qoopo.framework.date.FechasUtil;

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

    public static Condition fechaHoy(Field field) {
        return Condition.builder().name(field.getNombre() + ": Hoy").field(field).function(Function.ENTRE)
                .value(new Value(FechasUtil.fechaInicio(LocalDateTime.now())))
                .value2(new Value(FechasUtil.fechaFin(LocalDateTime.now()))).build();
    }

    public static Condition fechaEsteMes(Field field) {
        return Condition.builder().name(field.getNombre() + ": Este mes").field(field).function(Function.ENTRE)
                .value(new Value(FechasUtil.fechaInicioMes(LocalDateTime.now())))
                .value2(new Value(FechasUtil.fechaFinMes(LocalDateTime.now()))).build();
    }

    public static Condition fechaMesAnterior(Field field) {
        return Condition.builder().name(field.getNombre() + ": Mes Anterior").field(field).function(Function.ENTRE)
                .value(new Value(FechasUtil.fechaInicioMes(LocalDateTime.now().plusMonths(-1))))
                .value2(new Value(FechasUtil.fechaFinMes(LocalDateTime.now().plusMonths(-1)))).build();
    }

    public static Condition fechaEsteAnio(Field field) {
        return Condition.builder().name(field.getNombre() + ": Este año").field(field).function(Function.ENTRE)
                .value(new Value(FechasUtil.fechaInicioAnio(LocalDateTime.now())))
                .value2(new Value(FechasUtil.fechaFinAnio(LocalDateTime.now()))).build();
    }

    public static Condition fechaAnioAnterior(Field field) {
        return Condition.builder().name(field.getNombre() + ": Año anterior").field(field).function(Function.ENTRE)
                .value(new Value(FechasUtil.fechaInicioAnio(LocalDateTime.now().plusYears(-1))))
                .value2(new Value(FechasUtil.fechaFinAnio(LocalDateTime.now().plusYears(-1)))).build();
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
