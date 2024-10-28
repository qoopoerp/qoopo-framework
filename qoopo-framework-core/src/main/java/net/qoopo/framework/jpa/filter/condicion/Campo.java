package net.qoopo.framework.jpa.filter.condicion;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campo implements Serializable {

    public static final int STRING = 1;
    public static final int BOLEANO = 2;
    public static final int FECHA = 3;
    public static final int NUMERICO = 4;
    public static final int LONG = 5;
    public static final int INTEGER = 6;
    private int tipo = STRING;
    // el nombre que se muestra al usuario
    private String nombre;
    // el nombre del campo en formato JPA para armar la consulta
    private String nombreJPA;

    public Campo() {
    }

    public Campo(String nombre, String nombreJPA) {
        this.nombre = nombre;
        this.nombreJPA = nombreJPA;
        tipo = STRING;
    }

    public Campo(int tipo, String nombre, String nombreJPA) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.nombreJPA = nombreJPA;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nombre);
        hash = 37 * hash + Objects.hashCode(this.nombreJPA);
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
        final Campo other = (Campo) obj;

        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.nombreJPA, other.nombreJPA)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Campo { nombre=" + nombre + ", nombreJPA=" + nombreJPA + '}';
    }

}
