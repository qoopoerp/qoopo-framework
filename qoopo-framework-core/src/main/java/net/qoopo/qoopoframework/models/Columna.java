package net.qoopo.qoopoframework.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Columna implements Serializable {

    public static final int TEXTO = 1;
    public static final int FECHA = 2;
    public static final int FECHA_HUMANA = 3;
    public static final int DECIMAL = 4;
    public static final int MONEDA = 5;

    private int tipo; // el tipo de la columna
    private int codigoTextoNombre; // el tipo del columna
    private String columna; // el columna
    private String columnaOrdenar; // columna para ordenar
    private String clase; // clase CSS

    public Columna(int tipo, int codigoTextoNombre, String texto) {
        this.tipo = tipo;
        this.codigoTextoNombre = codigoTextoNombre;
        this.columna = texto;
        this.columnaOrdenar = columna;
    }

    public Columna(int tipo, int codigoTextoNombre, String columna, String clase) {
        this.tipo = tipo;
        this.codigoTextoNombre = codigoTextoNombre;
        this.columna = columna;
        this.columnaOrdenar = columna;
        this.clase = clase;
    }

    public Columna(int tipo, int codigoTextoNombre, String columna, String columnaOrdenar, String clase) {
        this.tipo = tipo;
        this.codigoTextoNombre = codigoTextoNombre;
        this.columna = columna;
        this.columnaOrdenar = columnaOrdenar;
        this.clase = clase;
    }

}
