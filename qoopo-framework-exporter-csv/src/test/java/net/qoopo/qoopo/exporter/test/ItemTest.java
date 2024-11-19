package net.qoopo.qoopo.exporter.test;

import java.math.BigDecimal;

import net.qoopo.framework.exporter.Exportable;
import net.qoopo.framework.exporter.Exporter;
import net.qoopo.framework.exporter.Importable;
import net.qoopo.framework.exporter.Importer;

public class ItemTest implements Exportable, Importable {

    private String codigo;
    private String nombre;
    private Integer entero;
    private BigDecimal valorDecimal;

    public ItemTest() {
    }

    public ItemTest(String codigo, String nombre, Integer entero, BigDecimal valorDecimal) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.entero = entero;
        this.valorDecimal = valorDecimal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEntero() {
        return entero;
    }

    public void setEntero(Integer entero) {
        this.entero = entero;
    }

    public BigDecimal getValorDecimal() {
        return valorDecimal;
    }

    public void setValorDecimal(BigDecimal valorDecimal) {
        this.valorDecimal = valorDecimal;
    }

    @Override
    public void exportar(Exporter exp) {
        exp.set("codigo", this.codigo);
        exp.set("nombre", this.nombre);
        exp.set("entero", this.entero);
        exp.set("decimal", this.valorDecimal);
    }

    @Override
    public void importar(Importer imp) {
        this.setCodigo((String) imp.get("codigo"));
        this.setNombre((String) imp.get("nombre"));
        this.setEntero(Integer.valueOf((String) imp.get("entero")));
        this.setValorDecimal(new BigDecimal((String) imp.get("decimal")));
    }

    @Override
    public String toString() {
        return "ItemTest{" + "codigo=" + codigo + ", nombre=" + nombre + ", entero=" + entero + ", valorDecimal="
                + valorDecimal + '}';
    }

}
