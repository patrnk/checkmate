package io.github.patrnk.checkmate;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author vergeev
 */
public class CheckSceneController implements Initializable {
    
    private Test test = null;
    
    public void setTest(Test test) {
        this.test = test;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (test == null) {
            throw new AssertionError("You must set test before showing the scene.");
        }
    }
}
