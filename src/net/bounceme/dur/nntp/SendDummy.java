package net.bounceme.dur.nntp;

import gnu.mail.providers.smtp.SMTPTransport;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendDummy {

    private Message message = null;
    private Session session = null;
    private Properties props = null;
    private SMTPTransport transport = null;

    public SendDummy() {
        try {
            sendMessage();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(SendDummy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(SendDummy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SendDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new SendDummy();
    }

    private void sendMessage() throws NoSuchProviderException, MessagingException, IOException {
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        props = PropertiesReader.getProps();
        props.list(System.out);
        System.out.println("\n========message follows==========\n");
        
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

            @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(props.getProperty("mail.smtp.email"), props.getProperty("mail.smtp.password"));
                    }
                });


        session.setDebug(true);
        message = new MimeMessage(session);
        String host = props.getProperty("mail.smtp.host");
        String user = props.getProperty("mail.smtp.user");
        String password = props.getProperty("mail.smtp.password");
        int port = Integer.parseInt(props.getProperty("mail.smtp.port"));
        System.out.println(host + user + password + port);
        message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user.email")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(props.getProperty("mail.smtp.user.email"), false));
        message.setText("hello gmail");
        System.out.println(message.getFrom().toString());
        System.out.println(message.getRecipients(Message.RecipientType.TO).toString());
        System.out.println(message.getContent().toString());
        transport = (SMTPTransport) session.getTransport("smtp");
        System.out.println("trying...");
        transport.connect(host, port, user, password);
        System.out.println("...connected");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
