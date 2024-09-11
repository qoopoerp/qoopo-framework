package net.qoopo.qoopoframework.core.db.filtro;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.qoopoframework.core.db.filtro.condicion.Condicion;
import net.qoopo.qoopoframework.core.util.QoopoBuilder;
import net.qoopo.util.db.jpa.Parametro;

/**
 * Clase usada para representar un filtro.
 *
 * Genera una consulta JPLSQL, en funcion de los datos almacenadas y las
 * condiciones asignadas
 *
 * @author alberto
 */
@Getter
@Setter
public class Filtro implements Serializable {

    private static final Logger log = Logger.getLogger("qoopo-jpa-filter");

    public static Builder getBuilder() {
        return new Builder();
    }

    private String selectObject = "o";
    private String nombre;
    // select o from tabla o
    private String tablaJPL;
    // condición raíz o principal, si hay más condiciones que se unen con un and o
    // or se agregan al elemento siguiente del nodo raíz
    private Condicion condicion;
    // condiciones posteriores como order by
    private String posterior = "";
    // la direccion de la orden
    private String orderDirection = "";

    public Filtro() {
    }

    public Filtro(String nombre) {
        this.nombre = nombre;
    }

    public Filtro(String nombre, String tablaJPL) {
        this.nombre = nombre;
        this.tablaJPL = tablaJPL;
    }

    public Filtro(String nombre, String tablaJPL, String posterior) {
        this.nombre = nombre;
        this.tablaJPL = tablaJPL;
        this.posterior = posterior;
    }

    public Filtro(String nombre, String selectObject, String tablaJPL, String posterior) {
        this.nombre = nombre;
        this.selectObject = selectObject;
        this.tablaJPL = tablaJPL;
        this.posterior = posterior;
    }

    public Filtro(String nombre, Condicion condicion) {
        this.nombre = nombre;
        this.condicion = condicion;
    }

    public Filtro(String nombre, String tablaJPL, Condicion condicion) {
        this.nombre = nombre;
        this.tablaJPL = tablaJPL;
        this.condicion = condicion;
    }

    public String getSelectObject() {
        return selectObject;
    }

    public void setSelectObject(String selectObject) {
        this.selectObject = selectObject;
    }

    public String getTablaJPL() {
        return tablaJPL;
    }

    public void setTablaJPL(String tablaJPL) {
        this.tablaJPL = tablaJPL;
    }

    public String getNombre() {
        return nombre;
    }

    public Filtro setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Condicion getCondicion() {
        return condicion;
    }

    public Filtro setCondicion(Condicion condicion) {
        this.condicion = condicion;
        return this;
    }

    public Parametro obtenerParametros(Parametro parametro) {
        if (condicion != null) {
            condicion.obtenerParametros(parametro);
        }
        return parametro;
    }

    public String getPosterior() {
        return posterior;
    }

    public void setPosterior(String posterior) {
        this.posterior = posterior;
    }

    /**
     * Construye el query jpl
     *
     * @return
     */
    public String buildQuery() {
        String query = null;
        try {
            if (condicion != null && !condicion.equals(Condicion.EMPTY)) {
                query = "select ".concat(this.selectObject).concat(" from ").concat(tablaJPL).concat(" o where ")
                        .concat(condicion.buildQuery()).concat(" ")
                        .concat(posterior).concat(" ").concat(!posterior.isEmpty() ? orderDirection : "");
            } else {
                query = "select ".concat(this.selectObject).concat(" from ").concat(tablaJPL).concat(" o ")
                        .concat(posterior).concat(" ").concat(!posterior.isEmpty() ? orderDirection : "");
            }
            log.log(Level.INFO, "Query Filtro=({0})", query);
            return query;
        } catch (Exception e) {
            //
        }

        return query;
    }

    /**
     * Construye el query jpl para contabilizar los resultados, no se usa el
     * criterio posterior
     *
     * @return
     */
    public String buildQueryCount() {
        try {
            if (condicion != null) {
                return "select count(o) from ".concat(tablaJPL).concat(" o where ").concat(condicion.buildQuery())
                        .concat(" ");
            } else {
                return "select count(o) from ".concat(tablaJPL).concat(" o ");
            }
        } catch (Exception e) {
            //
        }

        return null;
    }

    @Override
    public String toString() {
        return "Filtro{" + "nombre=" + nombre + ", tablaJPL=" + tablaJPL + ", condicion=" + condicion + ", posterior="
                + posterior + '}';
    }

    // /**
    // * Solo agrega una condición a la condición actual, sin agregar a la cola
    // *
    // * @param condicion
    // * @param tipoRelacion
    // */
    // public void agregarCondicionNoCola(Condicion condicion, int tipoRelacion) {
    // try {
    // if (this.condicion == null) {
    // setCondicion(condicion);
    // } else {
    // switch (tipoRelacion) {
    // case Condicion.RELACION_AND:
    // getCondicion().and(condicion);
    // break;
    // case Condicion.RELACION_OR:
    // getCondicion().or(condicion);
    // break;
    // default:
    // break;
    // }
    //
    // }
    // } catch (Exception e) {
    // //
    // }
    // }
    /**
     * Agrega una condicion a la cola de condiciones
     *
     * @param condicion
     * @param tipoRelacion
     */
    public void agregarCondicion(Condicion condicion, int tipoRelacion) {
        try {
            if (this.condicion == null || this.condicion.equals(Condicion.EMPTY)) {
                setCondicion(condicion);
            } else {
                switch (tipoRelacion) {
                    case Condicion.RELACION_AND:
                        getCondicion().and(condicion);
                        break;
                    case Condicion.RELACION_OR:
                        getCondicion().or(condicion);
                        break;
                    case Condicion.RELACION_EMPTY:
                        getCondicion().concat(condicion);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            // e
        }
    }

    public static class Builder implements QoopoBuilder<Filtro> {

        private String nombre;
        // select o from tabla o
        private String tablaJPL;
        // condición raíz o principal, si hay más condiciones que se unen con un and o
        // or se agregan al elemento siguiente del nodo raíz
        private Condicion condicion;
        // condiciones posteriores como order by
        private String posterior = "";

        public Builder() {
            //
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder tablaJPL(String tablaJPL) {
            this.tablaJPL = tablaJPL;
            return this;
        }

        public Builder condicion(Condicion condicion) {
            this.condicion = condicion;
            return this;
        }

        public Builder posterior(String posterior) {
            this.posterior = posterior;
            return this;
        }

        @Override
        public Filtro build() {
            Filtro t = new Filtro();
            t.setNombre(nombre);
            t.setTablaJPL(tablaJPL);
            t.setPosterior(posterior);
            t.setCondicion(condicion);
            return t;
        }

    }
}
