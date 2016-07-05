/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.patrnk.checkmate;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author vergeev
 */
public class CreateTestSceneController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCheckerFactories();
        factoryBox.setItems(getFactoryTitles(checkerFactories));
    }
    
    ObservableList<TestFactory> checkerFactories = FXCollections.observableArrayList();
    
    private void initializeCheckerFactories() {
        checkerFactories.add(new PermissiveTestFactory());
    }
    
    private ObservableList<String> getFactoryTitles(ObservableList<TestFactory> factories) {
        ObservableList<String> titles = FXCollections.observableArrayList();
        for (TestFactory factory : factories) {
            titles.add(factory.getTitle());
        }
        return titles;
    }
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField idField;
    
    @FXML
    private TextArea contentArea;
    
    @FXML
    private ComboBox<String> factoryBox;
    
    @FXML
    private Label testSummaryLabel;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private void saveButtonClicked(ActionEvent event) {
        TestInfo info = new TestInfo(nameField.getText(), 
            Long.valueOf(idField.getText()), contentArea.getText());
        try {
            Test test = new PermissiveTest(info);
        } catch (MalformedTestDescriptionException ex) {
            Logger.getLogger(CreateTestSceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AnswerNotProvidedException ex) {
            Logger.getLogger(CreateTestSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
