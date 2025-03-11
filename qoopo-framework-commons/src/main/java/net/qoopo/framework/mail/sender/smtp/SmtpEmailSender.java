package net.qoopo.framework.mail.sender.smtp;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import net.qoopo.framework.mail.Email;
import net.qoopo.framework.mail.EmailAccount;
import net.qoopo.framework.mail.sender.EmailSender;
import net.qoopo.framework.mail.sender.SendEmailResponse;

/**
 * Impelmentacion default de EmailSender
 * 
 * @author alberto
 */
public class SmtpEmailSender implements EmailSender {

    private static Logger log = Logger.getLogger("smtp-email-sender");

    public SmtpEmailSender() {
        //
    }

    // public static SendEmailResponse enviarEmail(Email email, EmailAccount cuenta)
    // {
    // try {
    // return enviarEmail(email, cuenta, false);
    // } catch (Exception e) {
    // log.log(Level.SEVERE, "Error al enviar mail :{0}", e.getLocalizedMessage());
    // log.log(Level.SEVERE, null, e);
    // }
    // return null;
    // }

    private static String cleanAddress(String email) {
        if (email.contains("<") && email.contains(">")) {
            email = email.substring(email.indexOf("<"), email.indexOf(">"));
            email = email.replace("<", "");
            email = email.replace(">", "");
        }
        return email;
    }

    public SendEmailResponse sendEmail(Email email, EmailAccount cuenta, HashMap<String, String> params) {
        try {
            return sendEmail(email, cuenta, params, false);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al enviar mail :{0}", e.getLocalizedMessage());
            log.log(Level.SEVERE, null, e);
        }
        return null;
    }

    public SendEmailResponse sendEmail(Email email, EmailAccount cuenta, HashMap<String, String> params,
            boolean lanzarError) throws Exception {
        Properties props;
        props = new Properties();
        props.put("mail.transport.protocol", cuenta.getServerProtocolo());
        props.put("mail.smtp.host", cuenta.getServerHost());
        props.put("mail.smtp.port", cuenta.getServerPuerto());
        props.put("mail.smtp.auth", cuenta.getServerauth());
        props.put("mail.smtp.starttls.enable", cuenta.getServerttls());
        SendEmailResponse respuesta = new SendEmailResponse();
        try {
            Session session = Session.getInstance(props);
            MimeMessage message = new MimeMessage(session);
            // si se definio un from se usa, caso contrario se usa la cuenta
            if (email.getFrom() != null && !email.getFrom().isEmpty()) {
                message.setFrom(new InternetAddress(email.getFrom()));
            } else {
                message.setFrom(new InternetAddress(cuenta.getUsuario()));
            }

            for (String destinatario : email.getDestinatarios()) {
                // en caso de no haber separado antes
                for (String unico : destinatario.split(",")) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(cleanAddress(unico.trim())));
                }
            }
            message.setSubject(email.getAsunto(), "utf-8");
            message.setSentDate(new Date());
            message.setContent(email.getCuerpo());
            Transport t = session.getTransport(cuenta.getServerProtocolo());
            t.connect(cuenta.getUsuario(), cuenta.getPassword());
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            respuesta.setEnviado(true);
            return respuesta;
        } catch (MessagingException e) {
            respuesta.setEnviado(false);
            respuesta.setMensaje(e.getLocalizedMessage());
            log.log(Level.SEVERE, "Error al enviar mail :{0}", e.getLocalizedMessage());
            log.log(Level.SEVERE, null, e);
            if (lanzarError) {
                throw e;
            }
        }
        return respuesta;
    }

}
