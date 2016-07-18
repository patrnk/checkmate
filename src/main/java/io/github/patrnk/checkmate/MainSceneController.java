package io.github.patrnk.checkmate;

import io.github.patrnk.checkmate.persistence.PersistenceManager;
import io.github.patrnk.checkmate.persistence.Record;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class MainSceneController implements Initializable {
    
    @FXML
    private AnchorPane anchor;
    
    @FXML
    private TableView<Record> testResultTable;
    
    @FXML
    private TableView<Test> testsTable;
    
    @FXML
    private TableColumn<Test, String> nameColumn;
    
    @FXML
    private TableColumn<Test, String> idColumn;   
    
    @FXML
    private Button checkButton;
    
    @FXML
    private void openCheckScene(ActionEvent event) {
        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/CheckScene.fxml"));
            root = (Parent)loader.load();
            final Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.onHiddenProperty().setValue(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    //anchor.setDisable(false);
                    ((Stage)anchor.getScene().getWindow()).show();
                }
            });
            ((Stage)anchor.getScene().getWindow()).hide();
            
            CheckSceneController controller = 
                ((CheckSceneController)loader.getController());
            Test selectedTest = testsTable.getSelectionModel().getSelectedItem();
            controller.setTest(selectedTest);
            
            stage.show();
            //anchor.setDisable(true);
        } catch (IOException e) {
            CmUtils.printExceptionAndExit(e);
        }
    }
    
    @FXML
    private void testResultTableClicked() {
        System.out.println(String.valueOf(testResultTable.getSelectionModel().getSelectedIndex()));
    }
    
    @FXML
    private void testsTableClicked() {
        Test selected = testsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            checkButton.setDisable(false);
        }
    }
    
    @FXML 
    private void openCreateTestScene(ActionEvent event) {
        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/CreateTestScene.fxml"));
            root = (Parent)loader.load();
            final Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();            
        } catch (IOException e) {
            CmUtils.printExceptionAndExit(e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTestsTable();
    }
    
    private void populateTestsTable() {
        nameColumn.setCellValueFactory(getNameColumnCellValueFactory());
        idColumn.setCellValueFactory(getIdColumnCellValueFactory());
        
        ObservableList<Test> tests = FXCollections.observableArrayList(PersistenceManager.getExistingTests());
        testsTable.setItems(tests);
    }
    
    private Callback<CellDataFeatures<Test, String>, ObservableValue<String>> 
    getNameColumnCellValueFactory() {
        return new Callback<CellDataFeatures<Test, String>, ObservableValue<String>>() { 
            @Override
            public ObservableValue<String> call(CellDataFeatures<Test, String> param) {
                String name = param.getValue().getInfo().getName();
                return new SimpleStringProperty(name);
            }
        };
    }
    
    private Callback<CellDataFeatures<Test, String>, ObservableValue<String>> 
    getIdColumnCellValueFactory() {
        return new Callback<CellDataFeatures<Test, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Test, String> param) {
                String id = param.getValue().getInfo().getId().toString();
                return new SimpleStringProperty(id);
            }
        };
    }
}
