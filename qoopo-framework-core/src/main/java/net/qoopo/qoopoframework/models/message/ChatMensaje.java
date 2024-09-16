package net.qoopo.qoopoframework.models.message;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMensaje implements Serializable {

    private String usuario;
    private LocalDateTime hora;
    private String mensaje;

    public ChatMensaje(String usuario, LocalDateTime hora, String mensaje) {
        this.usuario = usuario;
        this.hora = hora;
        this.mensaje = mensaje;
    }

}
