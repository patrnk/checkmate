package io.github.patrnk.checkmate;

import io.github.patrnk.checkmate.persistence.BadStudentIdException;
import io.github.patrnk.checkmate.persistence.BadStudentNameException;
import io.github.patrnk.checkmate.persistence.PersistenceManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author vergeev
 */
public class CheckSceneController implements Initializable {
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField idField;
    
    @FXML
    private TextArea answerArea;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private void checkButtonClicked(ActionEvent event) {
        try {
            if (test == null) {
                throw new NullPointerException("Test property "
                    + "of CheckSceneController must be set to non-null value.");
            }
            checkIfFieldsAreEmpty();
            
            String rawAnswers = answerArea.getText();
            List<TestAnswer> answers = AnswerParser.getTestAnswers(rawAnswers);
            List<TestAnswer> checkedAnswers = test.check(answers);
            String studentName = nameField.getText();
            String studentId = idField.getText();
            
            PersistenceManager.writeDownTestResults(
                studentName, studentId, checkedAnswers, test.getInfo().getId());
            nameField.getScene().getWindow().hide();
        } catch (BadTestInfoException ex) {
            String error = BadTestInfoException.getAppropriateErrorMessage(ex);
            errorLabel.setText(error);
        } catch (BadStudentNameException ex) {
            String error = "Имя не может быть очень длинным или пустым.";
            errorLabel.setText(error);
        } catch (BadStudentIdException ex) {
            String error = "Идентификатор не может быть очень длинным или пустым.";
            errorLabel.setText(error);
        } catch (IOException ex) {
            String error = "Не можем записать результаты. "
                + "Напишите разработчику: patrnk@gmail.com";
            errorLabel.setText(error);
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    private Test test = null;
    
    public void setTest(Test test) {
        this.test = test;
    }
}
