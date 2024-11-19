package net.qoopo.framework.filter.core.condition;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field implements Serializable {

    public static final int STRING = 1;
    public static final int BOLEANO = 2;
    public static final int FECHA = 3;
    public static final int NUMERICO = 4;
    public static final int LONG = 5;
    public static final int INTEGER = 6;
    private int tipo = STRING;
    // el nombre que se muestra al usuario
    private String nombre;
    // el nombre del campo en formato Jpa para armar la consulta
    private String nombreJpa;

    public Field() {
    }

    public Field(String nombre, String nombreJpa) {
        this.nombre = nombre;
        this.nombreJpa = nombreJpa;
        tipo = STRING;
    }

    public Field(int tipo, String nombre, String nombreJpa) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.nombreJpa = nombreJpa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nombre);
        hash = 37 * hash + Objects.hashCode(this.nombreJpa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Field other = (Field) obj;

        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.nombreJpa, other.nombreJpa)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Field { nombre=" + nombre + ", nombreJpa=" + nombreJpa + '}';
    }

}
