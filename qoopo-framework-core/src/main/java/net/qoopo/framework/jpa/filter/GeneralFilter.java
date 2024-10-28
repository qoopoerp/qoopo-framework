package net.qoopo.framework.jpa.filter;

import java.time.LocalDateTime;

import net.qoopo.framework.date.FechasUtil;
import net.qoopo.framework.jpa.filter.condicion.Campo;
import net.qoopo.framework.jpa.filter.condicion.Condicion;
import net.qoopo.framework.jpa.filter.condicion.Funcion;
import net.qoopo.framework.jpa.filter.condicion.Valor;

public class GeneralFilter {

    public static final Campo ARCHIVADO = new Campo(Campo.BOLEANO, "Archivado", "o.archived");
    public static final Campo EMPRESA = new Campo(Campo.LONG, "Empresa", "o.empresa.id");

    public static Condicion condicionEmpresa(Long idEmpresa) {
        return Condicion.getBuilder().campo(GeneralFilter.EMPRESA).funcion(Funcion.IGUAL)
                .valor(new Valor("idEmpresa", idEmpresa)).build();
    }

    public static Condicion condicionActivo() {
        return Condicion.open().concat(
                Condicion.getBuilder().campo(GeneralFilter.ARCHIVADO).funcion(Funcion.IGUAL)
                        .valor(new Valor("archived", Boolean.FALSE)).build()
                        .or(Condicion.getBuilder().campo(GeneralFilter.ARCHIVADO).funcion(Funcion.ES_NULO).build())
                        .cerrar())
                .setNombre("Activo");
    }

    public static Condicion condicionArchivado() {
        return Condicion.getBuilder().campo(GeneralFilter.ARCHIVADO).funcion(Funcion.IGUAL)
                .valor(new Valor("archived", Boolean.TRUE)).build().setNombre("Archivado");
    }

    public static Condicion fechaHoy(Campo campo) {
        return Condicion.getBuilder().nombre(campo.getNombre() + ": Hoy").campo(campo).funcion(Funcion.ENTRE)
                .valor(new Valor(FechasUtil.fechaInicio(LocalDateTime.now())))
                .valor2(new Valor(FechasUtil.fechaFin(LocalDateTime.now()))).build();
    }

    public static Condicion fechaEsteMes(Campo campo) {
        return Condicion.getBuilder().nombre(campo.getNombre() + ": Este mes").campo(campo).funcion(Funcion.ENTRE)
                .valor(new Valor(FechasUtil.fechaInicioMes(LocalDateTime.now())))
                .valor2(new Valor(FechasUtil.fechaFinMes(LocalDateTime.now()))).build();
    }

    public static Condicion fechaMesAnterior(Campo campo) {
        return Condicion.getBuilder().nombre(campo.getNombre() + ": Mes Anterior").campo(campo).funcion(Funcion.ENTRE)
                .valor(new Valor(FechasUtil.fechaInicioMes(LocalDateTime.now().plusMonths(-1))))
                .valor2(new Valor(FechasUtil.fechaFinMes(LocalDateTime.now().plusMonths(-1)))).build();
    }

    public static Condicion fechaEsteAnio(Campo campo) {
        return Condicion.getBuilder().nombre(campo.getNombre() + ": Este año").campo(campo).funcion(Funcion.ENTRE)
                .valor(new Valor(FechasUtil.fechaInicioAnio(LocalDateTime.now())))
                .valor2(new Valor(FechasUtil.fechaFinAnio(LocalDateTime.now()))).build();
    }

    public static Condicion fechaAnioAnterior(Campo campo) {
        return Condicion.getBuilder().nombre(campo.getNombre() + ": Año anterior").campo(campo).funcion(Funcion.ENTRE)
                .valor(new Valor(FechasUtil.fechaInicioAnio(LocalDateTime.now().plusYears(-1))))
                .valor2(new Valor(FechasUtil.fechaFinAnio(LocalDateTime.now().plusYears(-1)))).build();
    }

    /**
     * Lista todos los registros
     * 
     * @return
     */
    public static Filter all(String TABLA_JPL) {
        Filter filtro = new Filter();
        filtro.setNombre("Todos");
        filtro.setTablaJPL(TABLA_JPL);
        filtro.setPosterior("order by o.order");
        filtro.setOrderDirection("asc");
        return filtro;
    }

}
