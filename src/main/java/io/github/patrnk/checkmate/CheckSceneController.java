package io.github.patrnk.checkmate;

import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private void checkButtonClicked(ActionEvent event) {
        try {
            if (test == null) {
                throw new IllegalArgumentException();
            }
            checkInput();
            String rawAnswers = answerArea.getText();
            List<TestAnswer> answers = AnswerParser.getTestAnswers(rawAnswers);
            List<TestAnswer> checkedAnswers = test.check(answers);
            for (TestAnswer checkedAnswer : checkedAnswers) {
                System.out.println(checkedAnswer.getAnswer() + " : " + 
                    checkedAnswer.getGrade().toString());
            }
        } catch (IllegalArgumentException | ParseException ex) {
            //TODO: let user know about what's going on
        }
    }
    
    // TODO: replace IllegalArgumentException with multiple custom exceptions
    private void checkInput() throws IllegalArgumentException {
        if ("".equals(nameField.getText())) {
            throw new IllegalArgumentException();
        }
        if ("".equals(idField.getText())) {
            throw new IllegalArgumentException();
        }
        if ("".equals(answerArea.getText())) {
            throw new IllegalArgumentException();
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
