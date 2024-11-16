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

    public JpaParameters add(String JpaParameters, Object valor) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new JpaParameter(JpaParameters, valor));
        return this;
    }

    public JpaParameters add(String JpaParameters, Class clase, ParameterMode parameterMode) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new JpaParameter(JpaParameters, clase, parameterMode));
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

}
