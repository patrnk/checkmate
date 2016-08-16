package io.github.patrnk.checkmate;

import com.sun.mail.imap.IMAPMessage;
import io.github.patrnk.checkmate.persistence.BadStudentIdException;
import io.github.patrnk.checkmate.persistence.BadStudentNameException;
import io.github.patrnk.checkmate.test.Test;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private void checkButtonClicked() {
        if (test == null) {
            throw new AssertionError("Test property for this scene must be "
                + "non-null. Use setTest(). ");
        }
        String login = emailField.getText();
        String pass = passField.getText();
        String testId = test.getInfo().getId().toString();
        try {
            Mailbox mail = new Mailbox(login, pass);
            IMAPMessage result = mail.getTestResult(testId);
            while (result != null) {
                mail.writeDownTestResults(result, test);
                result = mail.getTestResult(testId);
            }
        } catch (AuthenticationFailedException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверный логин или пароль");
            alert.setContentText("Обратите внимание, что войти можно "
                + "только через Яндекс Почту.");
            alert.showAndWait();
        } catch (MessagingException | IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось подключиться");
            alert.setContentText("Проверьте интернет-соединение.");
            alert.showAndWait();
            Logger.getLogger(EmailCheckSceneController.class.getName())
                .log(Level.SEVERE, null, ex);
        } catch (BadStudentNameException | BadStudentIdException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("У учащегося слишком длинное имя или почта");
            alert.setContentText("Зайдите на почту через сайт и обработайте "
                + "сообщение вручную.");
            alert.showAndWait();
            Logger.getLogger(EmailCheckSceneController.class.getName()).log(
                Level.SEVERE, null, ex);
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