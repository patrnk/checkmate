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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author vergeev
 */
public class CreateTestSceneController implements Initializable {
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField idField;
    
    @FXML
    private TextArea contentArea;

    @FXML
    private Button saveButton;
    
    @FXML
    private ComboBox<TestFactory> factoryBox;

    @FXML
    private Label testSummaryLabel;
    
    @FXML
    private Label errorLabel;

    @FXML
    private void factoryBoxValueChanged(ActionEvent event) {
        saveButton.disableProperty().set(false);
        TestFactory selected = factoryBox.getSelectionModel().getSelectedItem();
        testSummaryLabel.setText(selected.getSummary());
    }
    
    @FXML
    private void saveButtonClicked(ActionEvent event) {
       try {
            TestInfo info = new TestInfo(nameField.getText(), 
                idField.getText(), contentArea.getText());
            Test test = new PermissiveTest(info);
        } catch (BadTestInfoException ex) {
            showAppropriateError(ex);
        }
}
    
    private void showAppropriateError(BadTestInfoException ex) {
        if (ex.getClass().equals(BadTestNameException.class)) {
            errorLabel.setText("Плохо задано имя теста. "
                + "Оно не может быть пустым или очень длинным.");
        } else
        if (ex.getClass().equals(BadTestIdException.class)) {
            errorLabel.setText("Идентификатор должен быть целым числом. "
                + "Убедитесь, что он уникален.");
        } else
        if (ex.getClass().equals(MalformedTestDescriptionException.class)) {
            MalformedTestDescriptionException specificEx = 
                (MalformedTestDescriptionException) ex;
            errorLabel.setText("Нарушен формат записи ответа (см. строку" 
                + specificEx.getBadLine() + "). "
                + "Вот пример правильной записи: \"12)abc\".");
        } else
        if (ex.getClass().equals(AnswerNotProvidedException.class)) {
            AnswerNotProvidedException specificEx = 
                (AnswerNotProvidedException) ex;
            errorLabel.setText("Не задан правильный ответ для номера " 
                + specificEx.getQuestionNumber() + ".");
        } else {
            errorLabel.setText("Что-то пошло не так: " + ex.getClass().toString());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateFactoryBox();
    }

    private void populateFactoryBox() {
        factoryBox.setCellFactory(new Callback<ListView<TestFactory>, ListCell<TestFactory>>() {
            @Override
            public ListCell<TestFactory> call(ListView<TestFactory> param) {
                return getTestFactoryCell();
            }
        });
        factoryBox.setConverter(getTestFactoryStringConverter());
        factoryBox.setItems(getFactories());
    }

    private ListCell<TestFactory> getTestFactoryCell() {
        return new ListCell<TestFactory>() {
            @Override 
            public void updateItem(TestFactory item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        };
    }
    
    private StringConverter<TestFactory> getTestFactoryStringConverter() {
        return new StringConverter<TestFactory>() {
            @Override
            public String toString(TestFactory object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getTitle();
                }
            }

            @Override
            public TestFactory fromString(String string) {
                for (TestFactory factory : factoryBox.getItems()) {
                    if (factory.getTitle().equals(string)) {
                        return factory;
                    }
                }
                return null;
            }
            
        };
    }
    
    private ObservableList<TestFactory> getFactories() {
        ObservableList<TestFactory> factories = FXCollections.observableArrayList();
        factories.add(new PermissiveTestFactory());
        // add new factories here
        return factories;
    }
}
