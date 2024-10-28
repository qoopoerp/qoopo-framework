package net.qoopo.framework.mail;

//import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

/**
 * Clase que representa el Email
 *
 * @author alberto
 */
public class Email implements Serializable {

    private List<String> destinatarios;
    private String asunto;
    private String from;
    private MimeMultipart cuerpo;
    private String mensaje;
    private String tipoMensaje;
    private List<BodyPart> adjuntos;
    private boolean construir = true;

    public Email() {
        //
    }

    /**
     *
     * @param destinatario El destinatarios o destinatarios separados por ;
     * @param mensaje
     * @param asunto
     * @param from
     */
    public Email(String destinatario, String mensaje, String asunto, String from) {
        destinatarios = new ArrayList<>();
        StringTokenizer tk = new StringTokenizer(destinatario, ";");
        while (tk.hasMoreTokens()) {
            destinatarios.add(tk.nextToken());
        }
        this.mensaje = mensaje;
        this.asunto = asunto;
        this.from = from;
    }

    public Email(List<String> destinatarios, String mensaje, String asunto, String from) {
        this.destinatarios = destinatarios;
        this.asunto = asunto;
        this.from = from;
        this.mensaje = mensaje;
    }

    public Email(byte[] datos, String dataType) throws MessagingException {
        ByteArrayDataSource bads = new ByteArrayDataSource(datos, dataType);
        cuerpo = new MimeMultipart(bads);
        if (cuerpo != null) {
            System.out.println("EL CUERPO ES DIFERENTE DE NULO");
        } else {
            System.out.println("EL CUERPO ES NULO");
        }
        construir = false;
    }

    /**
     *
     * @param rutaArchivo includio el nombre del archivo
     * @param nombreArchivo solo el nombre del archivo, fines infomrativos
     * @throws MessagingException
     */
    public void agregarAdjunto(String rutaArchivo, String nombreArchivo) throws MessagingException {
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaArchivo)));
        adjunto.setFileName(nombreArchivo);

        if (adjuntos == null) {
            adjuntos = new ArrayList<BodyPart>();
        }
        adjuntos.add(adjunto);
    }

    public void agregarAdjunto(String nombreArchivo, byte[] datos, String mimeType) throws MessagingException {
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(new DataHandler(new ByteArrayDataSource(datos, mimeType)));
        adjunto.setFileName(nombreArchivo);

        if (adjuntos == null) {
            adjuntos = new ArrayList<BodyPart>();
        }
        adjuntos.add(adjunto);
    }

    /**
     * Perite agregar un objeto indicando su mimetype
     *
     * @param objeto
     * @param mimeType
     * @throws MessagingException
     */
    public void agregarAdjunto(Object objeto, String mimeType) throws MessagingException {
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(new DataHandler(objeto, mimeType));

        if (adjuntos == null) {
            adjuntos = new ArrayList<BodyPart>();
        }
        adjuntos.add(adjunto);
    }

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<String> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<BodyPart> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<BodyPart> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public MimeMultipart getCuerpo() {

        if (construir) {
            // Una MultiParte para agrupar texto e imagen.
            cuerpo = new MimeMultipart();

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            try {
                if (tipoMensaje != null && !tipoMensaje.equals("")) {
                    texto.setContent(mensaje, tipoMensaje);
                } else {
                    texto.setText(mensaje);
                }
            } catch (MessagingException ex) {
                Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                cuerpo.addBodyPart(texto);
            } catch (MessagingException ex) {
                Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (adjuntos != null) {
                for (BodyPart o : adjuntos) {
                    try {
                        cuerpo.addBodyPart(o);
                    } catch (MessagingException ex) {
                        Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return cuerpo;
    }

    public void setCuerpo(MimeMultipart cuerpo) {
        this.cuerpo = cuerpo;
    }

    /**
     * Indica el destinatarios o los destinatarios separados por ;
     *
     * @param destinatario
     */
    public void setDestinatario(String destinatario) {
        destinatarios = new ArrayList<String>();
//        destinatarios.add(destinatario);

        StringTokenizer tk = new StringTokenizer(destinatario, ";");
        while (tk.hasMoreTokens()) {
            destinatarios.add(tk.nextToken());
        }
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }
}
