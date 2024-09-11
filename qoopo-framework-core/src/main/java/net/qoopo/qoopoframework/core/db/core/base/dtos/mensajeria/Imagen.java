package net.qoopo.qoopoframework.core.db.core.base.dtos.mensajeria;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Imagen implements Serializable {

    private String imagen;
    private String user;
    private boolean updateList;

    public Imagen() {
    }

    public Imagen(String text) {
        this.imagen = text;
    }

    public Imagen(String imagen, boolean updateList) {
        this.imagen = imagen;
        this.updateList = updateList;
    }

    public Imagen(String user, String imagen, boolean updateList) {
        this.imagen = imagen;
        this.user = user;
        this.updateList = updateList;
    }

    public Imagen setText(String text) {
        this.imagen = text;
        return this;
    }

    public Imagen setUser(String user) {
        this.user = user;
        return this;
    }

}
