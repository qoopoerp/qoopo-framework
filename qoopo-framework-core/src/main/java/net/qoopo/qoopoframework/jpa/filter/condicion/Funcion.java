package net.qoopo.qoopoframework.jpa.filter.condicion;

import java.io.Serializable;
import java.util.Objects;

/**
 * La función de comparación para el query (mayor que, menor que, igual a, etc)
 *
 * @author alberto
 */
public class Funcion implements Serializable {

    public static final Funcion IGUAL = new Funcion("Igual", "=");
    public static final Funcion DIFERENTE = new Funcion("Diferente", "<>");
    public static final Funcion CONTIENE = new Funcion("Contiene", "like", " lower( ", ") ");
    public static final Funcion NO_CONTIENE = new Funcion("No Contiene", "not like", " lower( ", ") ");
    public static final Funcion MAYOR_QUE = new Funcion("Mayor que", ">");
    public static final Funcion MENOR_QUE = new Funcion("Menor que", "<");
    public static final Funcion MAYOR_O_IGUAL_QUE = new Funcion("Mayor o igual que", ">=");
    public static final Funcion MENOR_O_IGUAL_QUE = new Funcion("Menor o igual que", "<=");
    public static final Funcion ENTRE = new Funcion("Valor entre", "between");

    public static final Funcion ES_NULO = new Funcion("Es nulo", "is null");
    public static final Funcion NO_ES_NULO = new Funcion("No es nulo", "is not null");

    public static final Funcion ES_VERDADERO = new Funcion("Es Verdadero", "= true");
    public static final Funcion ES_FALSO = new Funcion("Es Falso", "= false");

    // el nombre que se muestra al usuario
    private String nombre;
    // el nombre del campo en formato JPA para armar la consulta
    private String nombreJPA;
    private String antesCampo = "";
    private String despuesCampo = "";

    public Funcion() {
    }

    public Funcion(String nombre, String nombreJPA) {
        this.nombre = nombre;
        this.nombreJPA = nombreJPA;
    }

    public Funcion(String nombre, String nombreJPA, String antesCampo, String despuesCampo) {
        this.nombre = nombre;
        this.nombreJPA = nombreJPA;
        this.antesCampo = antesCampo;
        this.despuesCampo = despuesCampo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreJPA() {
        return nombreJPA;
    }

    public void setNombreJPA(String nombreJPA) {
        this.nombreJPA = nombreJPA;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.nombreJPA);
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
        final Funcion other = (Funcion) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return Objects.equals(this.nombreJPA, other.nombreJPA);
    }

    public String getAntesCampo() {
        return antesCampo;
    }

    public void setAntesCampo(String antesCampo) {
        this.antesCampo = antesCampo;
    }

    public String getDespuesCampo() {
        return despuesCampo;
    }

    public void setDespuesCampo(String despuesCampo) {
        this.despuesCampo = despuesCampo;
    }

    @Override
    public String toString() {
        return "Funcion{" + "nombre=" + nombre + ", nombreJPA=" + nombreJPA + '}';
    }

}
