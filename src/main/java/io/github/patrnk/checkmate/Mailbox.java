package io.github.patrnk.checkmate;

import com.sun.mail.imap.IMAPMessage;
import io.github.patrnk.checkmate.persistence.BadStudentIdException;
import io.github.patrnk.checkmate.persistence.BadStudentNameException;
import io.github.patrnk.checkmate.persistence.PersistenceManager;
import io.github.patrnk.checkmate.test.AnswerFormatter;
import io.github.patrnk.checkmate.test.Test;
import io.github.patrnk.checkmate.test.TestAnswer;
import io.github.patrnk.checkmate.test.exception.BadTestInfoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import org.jsoup.Jsoup;


public final class Mailbox {
    
    private final Folder inbox;
    
    public Mailbox(String login, String password) 
            throws NoSuchProviderException, AuthenticationFailedException, 
                   MessagingException {
        Store store = this.getStore();
        store.connect("imap.yandex.ru", 993, login, password);
        this.inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
    }
    
    private Store getStore() throws NoSuchProviderException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.ssl.trust", "imap.yandex.ru");
        props.setProperty("mail.imaps.connectionpoolsize", "10");
        Session session = Session.getDefaultInstance(props, null);
        return session.getStore();
    }
    
    /**
     * Retrieves all the mail relating to the test and grades every one of them.
     * @param test the test to which the results are related.
     * @throws MessagingException
     * @throws IOException
     */
    public void retrieveAndGradeAndStoreTestResults(Test test) 
            throws MessagingException, IOException {
        String testId = test.getInfo().getId().toString();
        List<Message> testMessages = getTestMessages(testId);
        gradeAndStoreResults(testMessages, test);
        inbox.close(true);
    }
    
        
    /**
     * Gets messages related to the test.
     * @param testId id of the test to which messages are related.
     * @return messages related to the test.
     * @throws MessagingException
     */
    private List<Message> getTestMessages(String testId) 
            throws MessagingException {
        FlagTerm unseenFlag = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        Message unreadMessages[] = inbox.search(unseenFlag);
        List<Message> testMessages = new ArrayList();
        for (Message unreadMessage : unreadMessages) {
            ((IMAPMessage)unreadMessage).setPeek(true);
            String messageTestId = unreadMessage.getSubject().split(" ")[0];
            if (testId.equals(messageTestId)) {
                testMessages.add(unreadMessage);
            }
        }
        return testMessages;
    }
    
    private void gradeAndStoreResults(List<Message> results, Test test) 
            throws MessagingException, IOException {
        for (Message result : results) {
            try {
                gradeAndStoreResult(result, test);
            } catch (BadStudentNameException | BadStudentIdException ex) {
                Logger.getLogger(Mailbox.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void gradeAndStoreResult(Message result, Test test) 
            throws BadStudentNameException, BadStudentIdException, 
                  MessagingException, IOException {
        String studentName = getStudentName(result.getSubject());
        String studentId = getEmailAddress(result.getFrom()[0]);
        String messageText = getTextFromMessage(result).trim();
        AnswerFormatter formatter = new AnswerFormatter();
        try {
            List<List<String>> rawAnswers 
                = formatter.getSeparatedLowerCaseAnswers(messageText);
            ArrayList<TestAnswer> studentAnswers 
                = formatter.getTestAnswers(rawAnswers);
            studentAnswers = test.check(studentAnswers);
            PersistenceManager.writeDownTestResults(studentName, studentId, 
                studentAnswers, test.getInfo().getId());
        } catch (BadTestInfoException ex) {
            studentName = "! " + studentName;
            String error = BadTestInfoException.getAppropriateErrorMessage(ex);
            error += "\nВот так выглядит письмо:\n" + messageText;
            PersistenceManager.writeDownTestResults(
                studentName, studentId, error, test.getInfo().getId());  
        }
        inbox.setFlags(new Message[] {result}, new Flags(Flags.Flag.SEEN), true);
    }
    
    /**
     * Gets name of a student.
     * The name of a student is the whole message subject after test id,
     *      separated from it by a space.
     * @param message which contains student name in its subject
     * @return name of a student or default string if subject doesn't 
     *      contain a space.
     */
    private String getStudentName(String subject) {
        Integer spacePosition = subject.indexOf(" ");
        if (spacePosition == -1) {
            return "(имя не указано)";
        }
        return subject.substring(spacePosition + 1);
    }
    
    /**
     * Parses out email address from address string.
     * Assumes that email is written between < and >.
     * @param address address to parse.
     * @return email address.
     * @throws IllegalArgumentException if one of the symbols <, > is missing.
     */
    private String getEmailAddress(Address address) {
        String rawAddress = address.toString();
        Integer startingIndex = rawAddress.indexOf('<') + 1;
        Integer endingIndex = rawAddress.indexOf('>');
        if (startingIndex == 0 || endingIndex == -1) {
            throw new IllegalArgumentException(
                "One of the symbols <, > is missing in the address: " + 
                    address.toString());
        }
        return rawAddress.substring(startingIndex, endingIndex);
    }
    
    private String getTextFromMessage(Message message) 
            throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) 
        throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                MimeMultipart content = (MimeMultipart)bodyPart.getContent();
                result = result + getTextFromMimeMultipart(content);
            }
        }
        return result;
    }
}
