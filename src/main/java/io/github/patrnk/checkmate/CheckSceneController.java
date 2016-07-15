package io.github.patrnk.checkmate;

import io.github.patrnk.checkmate.persistence.BadStudentIdException;
import io.github.patrnk.checkmate.persistence.BadStudentNameException;
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
        } catch (BadStudentNameException | BadStudentIdException ex) {
            //TODO: let user know about what's going on
            System.out.println(ex);
        } catch (BadTestInfoException ex) {
            
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
