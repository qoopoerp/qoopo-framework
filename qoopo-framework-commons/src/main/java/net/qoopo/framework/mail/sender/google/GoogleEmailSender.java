/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.qoopo.framework.mail.sender.google;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import net.qoopo.framework.mail.Email;
import net.qoopo.framework.mail.EmailAccount;
import net.qoopo.framework.mail.sender.EmailSender;
import net.qoopo.framework.mail.sender.SendEmailResponse;
//import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author noroot
 */
public class GoogleEmailSender implements EmailSender {
    
    private static Logger log = Logger.getLogger("google-email-sender");
    
    //utl autenticaicon google
    //https://developers.google.com/identity/gsi/web/guides/get-google-api-clientid?hl=es-419
    //cuando haga pruebas des localhost
    //Punto clave: Configura el encabezado Referrer-Policy: no-referrer-when-downgrade cuando realices pruebas con http y localhost.
    
    
    @Override
    public SendEmailResponse sendEmail(Email email, EmailAccount cuenta, HashMap<String, String> params, boolean lanzarError) throws Exception {
        
        SendEmailResponse respuesta = new SendEmailResponse();

        /* Load pre-authorized user credentials from the environment.
           TODO(developer) - See https://developers.google.com/identity for
            guides on implementing OAuth2 for your application.*/
        
        
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(GmailScopes.GMAIL_SEND);
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        // Create the gmail API client
        Gmail service = new Gmail.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName(params.get("APP"))
                .build();

        // Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        // si se definio un from se usa, caso contrario se usa la cuenta
        if (email.getFrom() != null && !email.getFrom().isEmpty()) {
            mimeMessage.setFrom(new InternetAddress(email.getFrom()));
        } else {
            mimeMessage.setFrom(new InternetAddress(cuenta.getUsuario()));
        }
        
        for (String destinatario : email.getDestinatarios()) {
            // en caso de no haber separado antes
            for (String unico : destinatario.split(",")) {
                mimeMessage.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(cleanAddress(unico.trim())));
            }
        }
        mimeMessage.setSubject(email.getAsunto());
        mimeMessage.setSentDate(new Date());
        mimeMessage.setContent(email.getCuerpo());

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getEncoder().encodeToString(rawMessageBytes);  //Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        
        try {
            // Create send message
            message = service.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
//            return message;
            return respuesta;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
        return null;
    }
    
    @Override
    public SendEmailResponse sendEmail(Email email, EmailAccount cuenta, HashMap<String, String> params) {
        try {
            return sendEmail(email, cuenta, params, false);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al enviar mail :{0}", e.getLocalizedMessage());
            log.log(Level.SEVERE, null, e);
        }
        return null;
        
    }
    
    private static String cleanAddress(String email) {
        if (email.contains("<") && email.contains(">")) {
            email = email.substring(email.indexOf("<"), email.indexOf(">"));
            email = email.replace("<", "");
            email = email.replace(">", "");
        }
        return email;
    }
}
