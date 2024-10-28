package net.qoopo.framework.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpcionBase implements Serializable {

    public final static OpcionBase DEFAULT = new OpcionBase(-1000, 1797, "Default");
    public final static int SIN_GRUPO_CODE = -1001;
    public final static OpcionBase SIN_GRUPO = new OpcionBase(SIN_GRUPO_CODE, 1914, "Sin grupo");

    private int codigo; // el codigo que se usa para evaluar en las entidades
    private int codigoTexto; // el codigo del texto
    private String texto; // el texto
    private int nivel = 1; // el nivel al que corresponde. Ejemplo en una factura los valores de la factura
                           // son de nivel 1, los del detalle de factura son nivel 2

    public OpcionBase(int codigo, int codigoTexto, String texto) {
        this.codigo = codigo;
        this.codigoTexto = codigoTexto;
        this.texto = texto;
    }

    public OpcionBase(int codigo, int codigoTexto, String texto, int nivel) {
        this.codigo = codigo;
        this.codigoTexto = codigoTexto;
        this.texto = texto;
        this.nivel = nivel;
    }

}
