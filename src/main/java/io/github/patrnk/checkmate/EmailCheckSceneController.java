package io.github.patrnk.checkmate;

import io.github.patrnk.checkmate.test.Test;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class EmailCheckSceneController implements Initializable {

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passField;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private void checkButtonClicked() {
        statusLabel.setVisible(true);
        statusLabel.setText("Проверяем...");
        if (test == null) {
            throw new AssertionError("Test property for this scene must be "
                + "non-null. Use setTest(). ");
        }
        String login = emailField.getText();
        String pass = passField.getText();
        try {
            Mailbox mail = new Mailbox(login, pass);
            mail.retrieveAndGradeAndStoreTestResults(test);
            statusLabel.getScene().getWindow().hide();
        } catch (AuthenticationFailedException ex) {
            statusLabel.setVisible(true);
            statusLabel.setText("Неверный логин или пароль.");
        } catch (MessagingException | IOException ex) {
            statusLabel.setVisible(true);
            statusLabel.setText("Не удалось подключиться.");
            Logger.getLogger(EmailCheckSceneController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private Test test = null;
    
    public void setTest(Test test) {
        this.test = test;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }    
    
}

// sample code
//    private void AuthorizeEmail(String login, String password) {
//        Properties props = new Properties();
//        props.setProperty("mail.store.protocol", "imaps");
//        props.setProperty("mail.imaps.ssl.trust", "imap.yandex.ru");
//        props.setProperty("mail.imaps.connectionpoolsize", "10");
//        Session session = Session.getDefaultInstance(props, null);
//        Store store;
//        try {
//            store = session.getStore();
//            System.out.println("got storage.");
//            store.connect("imap.yandex.ru", 993, login, password);
//            System.out.println("connected.");
//            Folder inbox = store.getFolder("INBOX");
//            inbox.open(Folder.READ_WRITE);
//            System.out.println("folder is opened.");
//            Message lastMessage = inbox.getMessage(inbox.getMessageCount());
//            ((IMAPMessage)lastMessage).setPeek(true); // this is how you prevent automatic SEEN flag
//            System.out.println("got message.");
//            Address[] from = lastMessage.getFrom();
//            for (Address f : from) {
//                System.out.println("FROM: " + f.toString().substring(f.toString().indexOf('<') + 1, f.toString().indexOf('>')));
//            }
//            Multipart mp = (Multipart) lastMessage.getContent();
//            FlagTerm seenFlag = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
//            Message unreadMessages[] = inbox.search(seenFlag);
//            System.out.println("SUBJECT:" + lastMessage.getSubject());
//            System.out.println(getTextFromMessage(lastMessage).trim());
//            System.out.println((unreadMessages.length));
//            inbox.setFlags(new Message[] {lastMessage}, new Flags(Flags.Flag.SEEN), true); // this is how you set SEEN flag by hand
//            inbox.close(true);
//        } catch (NoSuchProviderException ex) {
//            Logger.getLogger(EmailCheckSceneController.class.getName())
//                .log(Level.SEVERE, null, ex);
//        } catch (AuthenticationFailedException ex) {
//            statusLabel.setVisible(true);
//            statusLabel.setText("Неверный логин или пароль.");
//        } catch (MessagingException | IOException ex) {
//            statusLabel.setVisible(true);
//            statusLabel.setText("Не удалось подключиться.");
//            Logger.getLogger(EmailCheckSceneController.class.getName())
//                .log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            Logger.getLogger(EmailCheckSceneController.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }
//    
//    private String getTextFromMessage(Message message) throws Exception {
//        String result = "";
//        if (message.isMimeType("text/plain")) {
//            result = message.getContent().toString();
//        } else if (message.isMimeType("multipart/*")) {
//            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
//            result = getTextFromMimeMultipart(mimeMultipart);
//        }
//        return result;
//    }
//
//    private String getTextFromMimeMultipart(
//            MimeMultipart mimeMultipart) throws Exception {
//        String result = "";
//        int count = mimeMultipart.getCount();
//        for (int i = 0; i < count; i++) {
//            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
//            if (bodyPart.isMimeType("text/plain")) {
//                result = result + "\n" + bodyPart.getContent();
//                break; // without break same text appears twice in my tests
//            } else if (bodyPart.isMimeType("text/html")) {
//                String html = (String) bodyPart.getContent();
//                result = result + "\n" + Jsoup.parse(html).text();
//            } else if (bodyPart.getContent() instanceof MimeMultipart){
//                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
//            }
//        }
//        return result;
//    }