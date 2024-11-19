package net.qoopo.framework.filter.core.condition;

import java.io.Serializable;
import java.util.Objects;

/**
 * La función de comparación para el query (mayor que, menor que, igual a, etc)
 *
 * @author alberto
 */
public class Function implements Serializable {

    public static final Function IGUAL = new Function("Igual", "=");
    public static final Function DIFERENTE = new Function("Diferente", "<>");
    public static final Function CONTIENE = new Function("Contiene", "like", " lower( ", ") ");
    public static final Function NO_CONTIENE = new Function("No Contiene", "not like", " lower( ", ") ");
    public static final Function MAYOR_QUE = new Function("Mayor que", ">");
    public static final Function MENOR_QUE = new Function("Menor que", "<");
    public static final Function MAYOR_O_IGUAL_QUE = new Function("Mayor o igual que", ">=");
    public static final Function MENOR_O_IGUAL_QUE = new Function("Menor o igual que", "<=");
    public static final Function ENTRE = new Function("Valor entre", "between");

    public static final Function ES_NULO = new Function("Es nulo", "is null");
    public static final Function NO_ES_NULO = new Function("No es nulo", "is not null");

    public static final Function ES_VERDADERO = new Function("Es Verdadero", "= true");
    public static final Function ES_FALSO = new Function("Es Falso", "= false");

    // el nombre que se muestra al usuario
    private String nombre;
    // el nombre del campo en formato Jpa para armar la consulta
    private String nombreJpa;
    private String antesCampo = "";
    private String despuesCampo = "";

    public Function() {
    }

    public Function(String nombre, String nombreJpa) {
        this.nombre = nombre;
        this.nombreJpa = nombreJpa;
    }

    public Function(String nombre, String nombreJpa, String antesCampo, String despuesCampo) {
        this.nombre = nombre;
        this.nombreJpa = nombreJpa;
        this.antesCampo = antesCampo;
        this.despuesCampo = despuesCampo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreJpa() {
        return nombreJpa;
    }

    public void setNombreJpa(String nombreJpa) {
        this.nombreJpa = nombreJpa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.nombreJpa);
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
        final Function other = (Function) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return Objects.equals(this.nombreJpa, other.nombreJpa);
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
        return "Funcion{" + "nombre=" + nombre + ", nombreJpa=" + nombreJpa + '}';
    }

}
