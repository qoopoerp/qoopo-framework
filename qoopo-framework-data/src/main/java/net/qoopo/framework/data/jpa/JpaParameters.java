package net.qoopo.framework.data.jpa;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ParameterMode;

/**
 *
 * @author alberto
 */
public class JpaParameters {

    private List<JpaParameter> lista;

    public static JpaParameters get() {
        return new JpaParameters();
    }

    public JpaParameters() {
        lista = new ArrayList<>();
    }

    public JpaParameters add(String parameter, Object valor) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new JpaParameter(parameter, valor));
        return this;
    }

    public JpaParameters add(String parameter, Class clase, ParameterMode parameterMode) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new JpaParameter(parameter, clase, parameterMode));
        return this;
    }

    public JpaParameters add(int indice, Object valor) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new JpaParameter(indice, valor));
        return this;
    }

    public JpaParameters add(int indice, Class clase, ParameterMode parameterMode) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new JpaParameter(indice, clase, parameterMode));
        return this;
    }

    public JpaParameters add(JpaParameter JpaParameters) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(JpaParameters);
        return this;
    }

    public List<JpaParameter> getLista() {
        return lista;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (lista != null) {
            lista.forEach(c -> sb.append(c.getParameter() != null ? c.getParameter() : c.getIndice()).append(": ")
                    .append(c.getValor()).append(" , "));
        }
        sb.append("}");
        return sb.toString();
    }

}
