package io.github.patrnk.checkmate.controller;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;
import io.github.patrnk.checkmate.test.PermissiveTest;
import io.github.patrnk.checkmate.test.TestInfo;
import io.github.patrnk.checkmate.test.Test;
import io.github.patrnk.checkmate.test.TestFactory;
import io.github.patrnk.checkmate.test.PermissiveTestFactory;
import io.github.patrnk.checkmate.persistence.PersistenceManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

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
            TestFactory factory = factoryBox.getSelectionModel().getSelectedItem();
            Test test = factory.getTest(info);
            PersistenceManager.writeDownTest(test);
            this.contentArea.getScene().getWindow().hide();
        } catch (BadTestInfoException ex) {
            String error = BadTestInfoException.getAppropriateErrorMessage(ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText(error);
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не получилось записать новый тест на диск.");
            alert.setContentText("Попробуйте переместить программу в другое место.");
            alert.showAndWait();
            Logger.getLogger(CreateTestSceneController.class.getName())
                .log(Level.SEVERE, null, ex);
        }   
       
    }
    
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
