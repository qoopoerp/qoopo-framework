/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.qoopo.framework.mail.sender;

import java.util.HashMap;
import net.qoopo.framework.mail.Email;
import net.qoopo.framework.mail.EmailAccount;

/**
 *
 * @author noroot
 */
public interface EmailSender {

    public SendEmailResponse sendEmail(Email email, EmailAccount cuenta, HashMap<String,String> params);
    
    public SendEmailResponse sendEmail(Email email, EmailAccount cuenta, HashMap<String,String> params, boolean lanzarError) throws Exception;
}
