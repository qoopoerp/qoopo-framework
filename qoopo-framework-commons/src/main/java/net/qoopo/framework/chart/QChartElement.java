package net.qoopo.framework.chart;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public class QChartElement implements Comparable<QChartElement> {

    //se ordena en funciona de este valor
    private int indice;
    private String clave;
    private double valor;
    //los valores siguientes son para el grafico tipo burbuja
    private double valorX;
    private double valorY;

    public QChartElement(int indice, String clave, double valor) {
        this.indice = indice;
        this.clave = clave;
        this.valor = valor;
    }

    public QChartElement(int indice, String clave, double valorX, double valorY, double valorRadio) {
        this.indice = indice;
        this.clave = clave;
        this.valorX = valorX;
        this.valorY = valorY;
        this.valor = valorRadio;
    }

    public QChartElement() {
    }

    @Override
    public int compareTo(QChartElement o) {
        return Integer.valueOf(indice).compareTo(o.indice);
    }

}
