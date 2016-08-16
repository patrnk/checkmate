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
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import org.jsoup.Jsoup;

/**
 * TODO: make it return messages and parse messages in persistence manager
 */
public final class Mailbox {
    // A little convention: do not close inbox folder anywhere.
    
    private static final String PROVIDER = "imap.yandex.ru";
    
    private final Folder inbox;
    
    public Mailbox(String login, String password) 
            throws AuthenticationFailedException, MessagingException {
        Store store = this.getStore();
        store.connect(PROVIDER, 993, login, password);
        this.inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
    }
    
    private Store getStore() {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.ssl.trust", PROVIDER);
        try {
            Session session = Session.getInstance(props, null);
            return session.getStore();
        } catch (NoSuchProviderException ex) {
            throw new ProviderException(ex);
        }
    }
    
    /**
     * Gets the first unread message that has testId as a first word 
     *      in the subject.
     * Returns null if there are no such messages.
     * @param testId the first word in the subject of the message targeted.
     * @return the first unread message that has testId as a first word 
     *      in the subject, null if there are no such messages.
     */
    public IMAPMessage getTestResult(String testId) throws MessagingException {
        FlagTerm unseenFlag = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        IMAPMessage[] unreadMessages = (IMAPMessage[])inbox.search(unseenFlag);
        for (IMAPMessage message : unreadMessages) {
            if (testId.equals(message.getSubject().split(" ")[0])) {
                return message;
            }
        }
        return null;
    }
    
    public void writeDownTestResults(IMAPMessage result, Test test)
        throws BadStudentNameException, BadStudentIdException, 
               MessagingException, IOException {
        result.setPeek(true);
        MimeMessage result_copy = new MimeMessage(result);
        String studentName = getStudentName(result_copy.getSubject());
        String studentId = getEmailAddress(result_copy.getFrom()[0]);
        String messageText = getText(result_copy).trim();
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
    
    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) 
            throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            if (p.isMimeType("text/html")) {
                s = Jsoup.parse(s).text();
                AnswerFormatter f = new AnswerFormatter();
                s = f.recoverMissingQuestionSeparators(s);
            }
            return s;
        }
        if (p.isMimeType("multipart/alternative")) {
            // prefer plaintext over html
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else if (bp.isMimeType("text/html")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
}