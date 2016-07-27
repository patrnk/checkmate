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