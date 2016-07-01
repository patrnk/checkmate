/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.patrnk.checkmate;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author vergeev
 */
public class CreateTestSceneController implements Initializable {

    @FXML
    private TextArea contentArea;
    
    @FXML
    private void saveButtonClicked(ActionEvent event) {
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addCheckers();
    }    
    
    List<TestChecker> checkers = new ArrayList();
    
    /**
     * Initializes checkers list. 
     */
    private void addCheckers() {
        checkers.add(CheckerWithATwist.getInstance());
    }
}
