package net.qoopo.qoopoframework.models.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Conversacion implements Serializable {

    private List<ChatMensaje> lista;
    private String titulo;
    private String mensaje;

    public Conversacion() {
        lista = new ArrayList<>();
    }

    public Conversacion(String titulo) {
        this.titulo = titulo;
        lista = new ArrayList<>();
    }

    public void enviar(String id, String mensaje) {
        agregar(new ChatMensaje(id, LocalDateTime.now(), mensaje));
    }

    public void enviar(ChatMensaje mensaje) {
        agregar(mensaje);
    }

    private void agregar(ChatMensaje mensaje) {
        if (!lista.isEmpty() && lista.get(lista.size() - 1).getUsuario().equals(mensaje.getUsuario())) {
            lista.get(lista.size() - 1)
                    .setMensaje(lista.get(lista.size() - 1).getMensaje() + "\n" + mensaje.getMensaje());
        } else {
            lista.add(mensaje);
        }
    }

}
