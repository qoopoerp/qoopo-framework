package net.qoopo.framework.mail;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Representa informacion de la cuenta del email
 *
 * @author aigarcia
 */
@Getter
@Setter
public class EmailAccount implements Serializable {

    private String usuario;
    private String mailFrom;
    private String serverHost;
    private String serverPuerto;
    private String serverProtocolo;
    private String serverauth;
    private String serverttls;
    private String password;

    public EmailAccount() {
        //
    }

    public EmailAccount(String usuario, String mailFrom, String serverHost, String serverPuerto, String serverProtocolo,
            String serverauth, String serverttls, String password) {
        this.usuario = usuario;
        this.mailFrom = mailFrom;
        this.serverHost = serverHost;
        this.serverPuerto = serverPuerto;
        this.serverProtocolo = serverProtocolo;
        this.serverauth = serverauth;
        this.serverttls = serverttls;
        this.password = password;
    }

    @Override
    public String toString() {
        return "EmailAccount { usuario=" + usuario + ", serverHost="
                + serverHost + ", serverPuerto=" + serverPuerto + ", serverProtocolo=" + serverProtocolo
                + ", serverauth=" + serverauth + ", serverttls=" + serverttls + ", password=*******}";
    }

}
