package net.qoopo.framework.web.components.tree;

import java.io.Serializable;

public class TreeGroup implements Serializable {

    private String valor;

    public TreeGroup(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
