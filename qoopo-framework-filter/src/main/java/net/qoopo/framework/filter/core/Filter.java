package net.qoopo.framework.filter.core;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.filter.Util;
import net.qoopo.framework.filter.core.condition.Condition;

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
@Builder
@AllArgsConstructor
public class Filter implements Serializable {

    private static final Logger log = Logger.getLogger("qoopo-jpa-filter");

    private String selectObject = "o";
    private String name;
    // select o from tabla o
    private String collection;
    // condición raíz o principal, si hay más condiciones que se unen con un and o
    // or se agregan al elemento siguiente del nodo raíz
    private Condition condition;
    // condiciones posteriores como order by
    @Builder.Default
    private String next = "";
    // la direccion de la orden
    @Builder.Default
    private String orderDirection = "";

    public Filter() {
    }

    public Filter(String nombre) {
        this.name = nombre;
    }

    public Filter(String nombre, String tablaJPL) {
        this.name = nombre;
        this.collection = tablaJPL;
    }

    public Filter(String nombre, String tablaJPL, String posterior) {
        this.name = nombre;
        this.collection = tablaJPL;
        this.next = posterior;
    }

    public Filter(String nombre, String selectObject, String tablaJPL, String posterior) {
        this.name = nombre;
        this.selectObject = selectObject;
        this.collection = tablaJPL;
        this.next = posterior;
    }

    public Filter(String nombre, Condition condicion) {
        this.name = nombre;
        this.condition = condicion;
    }

    public Filter(String nombre, String tablaJPL, Condition condicion) {
        this.name = nombre;
        this.collection = tablaJPL;
        this.condition = condicion;
    }

    public String getSelectObject() {
        return selectObject;
    }

    public void setSelectObject(String selectObject) {
        this.selectObject = selectObject;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String tablaJPL) {
        this.collection = tablaJPL;
    }

    public String getName() {
        return name;
    }

    public Filter setName(String nombre) {
        this.name = nombre;
        return this;
    }

    public Filter setCondition(Condition condicion) {
        this.condition = condicion;
        return this;
    }

    public void setNext(String posterior) {
        this.next = posterior;
    }

    /**
     * Construye el query jpl
     *
     * @return
     */
    public String buildQuery() {
        String query = null;
        try {
            if (condition != null && !condition.equals(Condition.EMPTY)) {

                query = "select ".concat(this.selectObject).concat(" from ");
                query = query.concat(collection).concat(" o where ");
                query = query.concat(condition.buildQuery()).concat(" ");
                query = query.concat(Util.nvl(next)).concat(" ");
                query = query.concat(!next.isEmpty() ? Util.nvl(orderDirection) : "");

                // query = "select ".concat(this.selectObject).concat(" from
                // ").concat(collection).concat(" o where ")
                // .concat(condition.buildQuery()).concat(" ")
                // .concat(next).concat(" ").concat(!next.isEmpty() ? orderDirection : "");
            } else {
                query = "select ".concat(this.selectObject).concat(" from ").concat(collection).concat(" o ")
                        .concat(Util.nvl(next)).concat(" ").concat(!next.isEmpty() ? Util.nvl(orderDirection) : "");
            }
            log.log(Level.INFO, "Query Filtro=({0})", query);
            return query;
        } catch (Exception e) {
            e.printStackTrace();
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
        String query = null;
        try {
            if (condition != null) {
                query = "select count(o) from ".concat(collection).concat(" o where ").concat(condition.buildQuery())
                        .concat(" ");
            } else {
                query = "select count(o) from ".concat(collection).concat(" o ");
            }
            log.log(Level.INFO, "Query Filtro count=({0})", query);
            return query;
        } catch (Exception e) {
            //
        }

        return query;
    }

    @Override
    public String toString() {
        return "Filtro{" + "nombre=" + name + ", selectObject=" + selectObject + ", collection=" + collection
                + ", condicion=" + condition + ", posterior="
                + next + '}';
    }

    // /**
    // * Solo agrega una condición a la condición actual, sin agregar a la cola
    // *
    // * @param condicion
    // * @param tipoRelacion
    // */
    // public void agregarCondicionNoCola(Condition condicion, int tipoRelacion) {
    // try {
    // if (this.condicion == null) {
    // setCondicion(condicion);
    // } else {
    // switch (tipoRelacion) {
    // case Condition.RELACION_AND:
    // getCondicion().and(condicion);
    // break;
    // case Condition.RELACION_OR:
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
     * @param relationtype
     */
    public void appendCondition(Condition condicion, int relationtype) {
        try {
            if (this.condition == null || this.condition.equals(Condition.EMPTY)) {
                setCondition(condicion);
            } else {
                switch (relationtype) {
                    case Condition.RELACION_AND:
                        getCondition().and(condicion);
                        break;
                    case Condition.RELACION_OR:
                        getCondition().or(condicion);
                        break;
                    case Condition.RELACION_EMPTY:
                        getCondition().concat(condicion);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            // e
        }
    }

}
