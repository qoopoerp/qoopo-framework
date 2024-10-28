/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.qoopo.framework.mail;

import java.util.HashMap;
import net.qoopo.framework.mail.sender.google.GoogleEmailSender;

/**
 *
 * @author noroot
 */
public class Test {

    public static void main(String[] args) {
        try {
            Email email = new Email();
            email.setAsunto("Testing");
            email.setMensaje("Contenido del mensaje");
            email.setFrom("beto.garcia.dk@gmail.com");
            email.setDestinatario("alberto.garcia@qoopo.net");
            
            
            GoogleEmailSender sender = new GoogleEmailSender();
            
            HashMap<String,String> params= new HashMap();
            params.put("APP", "qoopo-util-commons");
            sender.sendEmail(email, null, params);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
