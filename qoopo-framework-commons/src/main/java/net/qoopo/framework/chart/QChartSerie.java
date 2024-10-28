package net.qoopo.framework.chart;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Representa una serie de datos para los Graficos
 *
 * @author alberto
 */
@Getter
@Setter
public class QChartSerie implements Comparable<QChartSerie> {

    private int indice;
    private String nombre;
    private boolean lineal;
    private List<QChartElement> datos;

    public QChartSerie() {
    }

    public QChartSerie(String nombre) {
        this.nombre = nombre;
    }

    public QChartSerie(int indice, String nombre) {
        this.indice = indice;
        this.nombre = nombre;
    }

    public QChartSerie(String nombre, boolean lineal) {
        this.nombre = nombre;
        this.lineal = lineal;
    }

    public QChartSerie(int indice, String nombre, boolean lineal) {
        this.indice = indice;
        this.nombre = nombre;
        this.lineal = lineal;
    }

    public QChartSerie(String nombre, List<QChartElement> datos) {
        this.nombre = nombre;
        this.datos = datos;
    }

    public QChartSerie(int indice, String nombre, List<QChartElement> datos) {
        this.indice = indice;
        this.nombre = nombre;
        this.datos = datos;
    }

    public QChartSerie(String nombre, boolean lineal, List<QChartElement> datos) {
        this.nombre = nombre;
        this.lineal = lineal;
        this.datos = datos;
    }

    public QChartSerie(int indice, String nombre, boolean lineal, List<QChartElement> datos) {
        this.indice = indice;
        this.nombre = nombre;
        this.lineal = lineal;
        this.datos = datos;
    }

    public void agregarDato(QChartElement dato) {
        if (datos == null) {
            datos = new ArrayList<>();
        }
        datos.add(dato);
    }

    @Override
    public int compareTo(QChartSerie o) {
        return Integer.valueOf(indice).compareTo(o.indice);
    }

}
