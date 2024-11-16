package net.qoopo.framework.data.jpa;

import java.io.Serializable;

import jakarta.persistence.ParameterMode;

/**
 *
 * @author alberto
 */

public class JpaParameter implements Serializable {

    private String JpaParameters;
    private transient Object valor;
    // usado para los JpaParameterss de los sp
    private int indice = 0;
    private Class parameterClass;
    private ParameterMode parameterMode;

    public JpaParameter() {
        //
    }

    public JpaParameter(String JpaParameters, Object valor) {
        this.JpaParameters = JpaParameters;
        this.valor = valor;
    }

    public JpaParameter(int indice, Object valor) {
        this.indice = indice;
        this.valor = valor;
    }

    public JpaParameter(String JpaParameters, Class parameterClass, ParameterMode parameterMode) {
        this.JpaParameters = JpaParameters;
        this.parameterClass = parameterClass;
        this.parameterMode = parameterMode;
    }

    public JpaParameter(int indice, Class parameterClass, ParameterMode parameterMode) {
        this.indice = indice;
        this.parameterClass = parameterClass;
        this.parameterMode = parameterMode;
    }

    public String getJpaParameters() {
        return JpaParameters;
    }

    public void setJpaParameters(String JpaParameters) {
        this.JpaParameters = JpaParameters;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.JpaParameters != null ? this.JpaParameters.hashCode() : 0);
        hash = 61 * hash + (this.valor != null ? this.valor.hashCode() : 0);
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
        final JpaParameter other = (JpaParameter) obj;
        if ((this.JpaParameters == null) ? (other.JpaParameters != null) : !this.JpaParameters.equals(other.JpaParameters)) {
            return false;
        }
        return !(this.valor != other.valor && (this.valor == null || !this.valor.equals(other.valor)));
    }

    @Override
    public String toString() {
        return "JpaParametersJpa{" + "JpaParameters=" + JpaParameters + ", valor=" + valor + '}';
    }

    public ParameterMode getParameterMode() {
        return parameterMode;
    }

    public void setParameterMode(ParameterMode parameterMode) {
        this.parameterMode = parameterMode;
    }

    public Class getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(Class parameterClass) {
        this.parameterClass = parameterClass;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

}
