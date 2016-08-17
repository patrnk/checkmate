package io.github.patrnk.checkmate.controller;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;
import io.github.patrnk.checkmate.test.AnswerFormatter;
import io.github.patrnk.checkmate.test.TestAnswer;
import io.github.patrnk.checkmate.test.Test;
import io.github.patrnk.checkmate.persistence.BadStudentIdException;
import io.github.patrnk.checkmate.persistence.BadStudentNameException;
import io.github.patrnk.checkmate.persistence.PersistenceManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CheckSceneController implements Initializable {
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField idField;
    
    @FXML
    private TextArea answerArea;
    
    @FXML
    private void checkButtonClicked(ActionEvent event) {
        try {
            if (test == null) {
                throw new NullPointerException("Test property "
                    + "of CheckSceneController must be set to non-null value.");
            }
            checkIfFieldsAreEmpty();
            
            AnswerFormatter formatter = new AnswerFormatter();
            String rawAnswers = answerArea.getText();
            List<List<String>> separatedAnswers 
                = formatter.getSeparatedLowerCaseAnswers(rawAnswers);
            ArrayList<TestAnswer> answers = formatter.getTestAnswers(separatedAnswers);
            ArrayList<TestAnswer> checkedAnswers = test.check(answers);
            String studentName = nameField.getText();
            String studentId = idField.getText();
            
            PersistenceManager.writeDownTestResults(
                studentName, studentId, checkedAnswers, test.getInfo().getId());
            nameField.getScene().getWindow().hide();
        } catch (BadTestInfoException ex) {
            String error = BadTestInfoException.getAppropriateErrorMessage(ex);
            showErrorAlert(error);
            Logger.getLogger(CheckSceneController.class.getName())
                .log(Level.SEVERE, null, ex);
        } catch (BadStudentNameException ex) {
            String error = "Имя не может быть очень длинным или пустым.";
            showErrorAlert(error);
        } catch (BadStudentIdException ex) {
            String error = "Идентификатор не может быть очень длинным или пустым.";
            showErrorAlert(error);
        } catch (IOException ex) {
            String error = "Не можем записать результаты.";
            showErrorAlert(error);
            Logger.getLogger(CheckSceneController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private void checkIfFieldsAreEmpty() 
            throws BadStudentNameException, BadStudentIdException {
        if ("".equals(nameField.getText())) {
            throw new BadStudentNameException();
        }
        if ("".equals(idField.getText())) {
            throw new BadStudentIdException();
        }
    }
    
    private void showErrorAlert(String error) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText(error);
            alert.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    private Test test = null;
    
    public void setTest(Test test) {
        this.test = test;
    }
}
