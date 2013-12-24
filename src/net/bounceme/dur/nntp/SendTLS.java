package net.bounceme.dur.nntp;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
 */
public class SendTLS {

    public SendTLS() {
        try {
            sendMessage();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(SendTLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(SendTLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SendTLS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new SendTLS();
    }

    private void sendMessage() throws NoSuchProviderException, MessagingException, IOException {
        final Properties props = PropertiesReader.getProps();
        props.list(System.out);
        System.out.println("\n========message follows==========\n");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(props.getProperty("mail.smtp.username"), props.getProperty("mail.smtp.password"));
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.smtp.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(props.getProperty("mail.smtp.username")));
            message.setSubject("Testing Subject");
            message.setText("Dear Google,\n\n please accept this authenticator.");
            Transport.send(message);
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}