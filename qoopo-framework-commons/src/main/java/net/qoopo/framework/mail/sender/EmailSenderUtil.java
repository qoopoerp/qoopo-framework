/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.qoopo.framework.mail.sender;

import net.qoopo.framework.mail.EmailAccount;
import net.qoopo.framework.mail.sender.google.GoogleEmailSender;
import net.qoopo.framework.mail.sender.smtp.SmtpEmailSender;

/**
 *
 * @author noroot
 */
public class EmailSenderUtil {

    /**
     * Devuelve un sender en funcion de la cuenta
     *
     * @param account
     * @return
     */
    public static EmailSender getSender(EmailAccount account) {
        /*
        if (account != null) {
            if (account.getServerHost().toLowerCase().contains("google") || account.getServerHost().toLowerCase().contains("gmail")) {
                return new GoogleEmailSender();
            }
        }
        */
        return new SmtpEmailSender();
    }
}
