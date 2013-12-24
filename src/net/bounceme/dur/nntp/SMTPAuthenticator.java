/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.dur.nntp;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author thufir
 */
class SMTPAuthenticator extends Authenticator {

    public SMTPAuthenticator() {
        super();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        Properties props = PropertiesReader.getProps();
        return new PasswordAuthentication(props.getProperty("mail.smtp.user.email"), props.getProperty("mail.smtp.password"));
    }
}
