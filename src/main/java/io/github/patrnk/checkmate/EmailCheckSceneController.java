package io.github.patrnk.checkmate;

import io.github.patrnk.checkmate.test.Test;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class EmailCheckSceneController implements Initializable {

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passField;
    
    @FXML
    private Button checkButton;
    
    @FXML
    private void enableCheckButton() {
        checkButton.setDisable(false);
    }
    
    @FXML
    private void checkButtonClicked() {
        
    }
    
    private Test test;
    
    public void setTest(Test test) {
        this.test = test;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }    
    
}
