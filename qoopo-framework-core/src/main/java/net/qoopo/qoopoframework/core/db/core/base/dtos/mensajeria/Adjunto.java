package net.qoopo.qoopoframework.core.db.core.base.dtos.mensajeria;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Adjunto implements Serializable {

    private String nombre;
    private String nombreRuta;
    private String tamanio;
    private byte[] content;
    private String mimetype;

    public Adjunto(String nombre, String nombreRuta, String tamanio) {
        this.nombre = nombre;
        this.nombreRuta = nombreRuta;
        this.tamanio = tamanio;
    }

    public Adjunto(String nombre, String tamanio, byte[] content, String mimetype) {
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.content = content;
        this.mimetype = mimetype;
    }

    public Adjunto() {
    }

}
