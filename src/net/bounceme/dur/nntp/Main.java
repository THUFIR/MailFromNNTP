package net.bounceme.dur.nntp;

import gnu.mail.providers.smtp.SMTPTransport;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;

public class Main {

    private final Logger log = Logger.getLogger(Main.class.getName());
    private Message message = null;
    private Session session = null;
    private Properties props = null;
    private Folder root = null;
    private List<Folder> folders = null;
    private Store store = null;
    private Folder folder = null;
    private List<Message> messages = null;
    private SMTPTransport transport = null;

    public Main() {
        connect();
    }

    /*
    public static void main(String[] args) {
        new Main();
    }

     * 
     */
    private void connect() {
        props = PropertiesReader.getProps();
        session = Session.getDefaultInstance(props);
        session.setDebug(true);

        String host = props.getProperty("smtp.host");
        String user = props.getProperty("smtp.host");
        String password = props.getProperty("mail.password");
        int port = 465;

        try {
            store = session.getStore(new URLName(props.getProperty("nntp.host")));
        } catch (NoSuchProviderException ex) {
            log.warning(ex.toString());
        }
        try {
            store.connect();
            root = store.getDefaultFolder();
            folders = Arrays.asList(root.listSubscribed());
            transport = (SMTPTransport) session.getTransport("smtp");
            for (Folder f : folders) {
                folder = root.getFolder(f.getFullName());
                folder.open(Folder.READ_ONLY);
                messages = Arrays.asList(folder.getMessages());
                for (Message message : messages) {
                    log.info(message.getSubject());
                    message.setFrom(new InternetAddress(props.getProperty("mail.user")));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(props.getProperty("mail.user"), false));
                    transport.connect(host, port, user, password);
                    log.info("connected");
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                }
                folder.close(true);
            }
            root.close(true);
            store.close();
        } catch (MessagingException ex) {
            log.warning(ex.toString());
        }
    }
}
