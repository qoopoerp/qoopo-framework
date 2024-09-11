package net.qoopo.qoopoframework.core.db.core.base.dtos;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @Builder
public class Estado implements Serializable {

    private int valor;
    private String label;
    private String color = "";

    /**
     * Indica si el estado se debe mostrar solo si el estado actual es igual a
     * este estado. Ejemplo de usos un estado Anulado, solo se debe mostrar si el
     * documento esta anulado
     */
    private boolean condicional = false;

    public Estado() {
        //
    }

    public Estado(int valor, String label) {
        this.valor = valor;
        this.label = label;
    }

    public Estado(int valor, String label, String color) {
        this.valor = valor;
        this.label = label;
        this.color = color;
    }

    public Estado(int valor, String label, boolean condicional) {
        this.valor = valor;
        this.label = label;
        this.condicional = condicional;
    }

    public Estado(int valor, String label, boolean condicional, String color) {
        this.valor = valor;
        this.label = label;
        this.condicional = condicional;
        this.color = color;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.valor;
        hash = 19 * hash + Objects.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estado other = (Estado) obj;
        if (this.valor != other.valor) {
            return false;
        }
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // return "Estado{" + "valor=" + valor + ", label=" + label + '}';
        return String.valueOf(valor);
    }

}
