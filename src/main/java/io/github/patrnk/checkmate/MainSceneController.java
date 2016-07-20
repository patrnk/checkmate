package io.github.patrnk.checkmate;

import io.github.patrnk.checkmate.persistence.PersistenceManager;
import io.github.patrnk.checkmate.persistence.Record;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class MainSceneController implements Initializable {
    
    @FXML
    private AnchorPane anchor; 
    
    @FXML
    private TableView<Test> testsTable;
    
    @FXML
    private TableColumn<Test, String> testNameColumn;
    
    @FXML
    private TableColumn<Test, String> testIdColumn;   
    
    @FXML
    private TableView<Record> testResultTable;
    
    @FXML
    private TableColumn<Record, String> studentNameColumn;
    
    @FXML
    private TableColumn<Record, String> studentIdColumn; 
    
    @FXML
    private Button checkButton;
    
    @FXML
    private Button viewTestButton;

    @FXML 
    private void openCreateTestScene(ActionEvent event) {
        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/CreateTestScene.fxml"));
            root = (Parent)loader.load();
            final Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.onHiddenProperty().setValue(showMainEventHandler());
            hideMainWindow();
            stage.show();            
        } catch (IOException e) {
            CmUtils.printExceptionAndExit(e);
        }
    }
    
    @FXML
    private void openCheckScene(ActionEvent event) {
        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/CheckScene.fxml"));
            root = (Parent)loader.load();
            final Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.onHiddenProperty().setValue(showMainEventHandler());
            hideMainWindow();
            
            CheckSceneController controller = 
                ((CheckSceneController)loader.getController());
            Test selectedTest = testsTable.getSelectionModel().getSelectedItem();
            controller.setTest(selectedTest);
            
            stage.show();
        } catch (IOException e) {
            CmUtils.printExceptionAndExit(e);
        }
    }
    
    private void openViewScene(String headText, String bodyText) {
        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/ViewScene.fxml"));
            root = (Parent)loader.load();
            final Stage stage = new Stage();
            stage.setTitle("Просмотр");
            stage.setScene(new Scene(root));
            
            ViewSceneController controller = 
                ((ViewSceneController)loader.getController());
            controller.setText(headText, bodyText);
            
            stage.show();
        } catch (IOException e) {
            CmUtils.printExceptionAndExit(e);
        }
    }
    
    private EventHandler<WindowEvent> showMainEventHandler() {
        return new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    showMainWindow();
                }
            };
    }
    
    private void hideMainWindow() {
        ((Stage)anchor.getScene().getWindow()).hide();
    }

    private void showMainWindow() {
        ((Stage)anchor.getScene().getWindow()).show();
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
            viewTestButton.setDisable(false);
        }
    }
    
    @FXML
    private void viewTestButtonClicked() {
        Test selected = testsTable.getSelectionModel().getSelectedItem();
        String headText = selected.getInfo().getName();
        String bodyText = selected.getInfo().getDescription();
        openViewScene(headText, bodyText);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setValueFactoriesTestsTable();
        setValueFactoriesTestResultTable();
        anchor.sceneProperty().addListener(populateTablesOnShownSceneListener());
    }
    
    private void setValueFactoriesTestsTable() {
        testNameColumn.setCellValueFactory(getTestNameColumnCellValueFactory());
        testIdColumn.setCellValueFactory(getTestIdColumnCellValueFactory());
    }
    
    private void setValueFactoriesTestResultTable() {
        studentNameColumn.setCellValueFactory(getStudentNameColumnCellValueFactory());
        studentIdColumn.setCellValueFactory(getStudentIdColumnCellValueFactory());
    }
    
    private ChangeListener<Scene> populateTablesOnShownSceneListener() {
        return new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable,
                    Scene oldValue, Scene newValue) {
                
                if (newValue != null) {
                    anchor.getScene().windowProperty()
                        .addListener(populateTablesOnShownWindowListener());
                }
            }
        };
    }
    
    private ChangeListener<Window> populateTablesOnShownWindowListener() {
        return new ChangeListener<Window>() {
            @Override
            public void changed(ObservableValue<? extends Window> observable, 
                Window oldValue, Window newValue) {
                
                if (newValue != null) {
                    ((Stage)anchor.getScene().getWindow())
                        .setOnShown(populateTablesOnShownHandler());
                }
            }
        }; 
    }
    
    private EventHandler<WindowEvent> populateTablesOnShownHandler() {
        return new EventHandler<WindowEvent>() {            
            @Override
            public void handle(WindowEvent event) {
                getTestsTableItems();
                getTestResultTableItems();
            }
        };
    }
    
    private void getTestsTableItems() {
        ObservableList<Test> tests = FXCollections
            .observableArrayList(PersistenceManager.getExistingTests());
        testsTable.setItems(tests);
    }
    
    private void getTestResultTableItems() {
        ObservableList<Record> testResult = FXCollections
            .observableArrayList(PersistenceManager.getExistingTestResults());
        testResultTable.setItems(testResult);
    }
    
    private Callback<CellDataFeatures<Test, String>, ObservableValue<String>> 
    getTestNameColumnCellValueFactory() {
        return new Callback<CellDataFeatures<Test, String>, ObservableValue<String>>() { 
            @Override
            public ObservableValue<String> call(CellDataFeatures<Test, String> param) {
                String name = param.getValue().getInfo().getName();
                return new SimpleStringProperty(name);
            }
        };
    }
    
    private Callback<CellDataFeatures<Test, String>, ObservableValue<String>> 
    getTestIdColumnCellValueFactory() {
        return new Callback<CellDataFeatures<Test, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Test, String> param) {
                String id = param.getValue().getInfo().getId().toString();
                return new SimpleStringProperty(id);
            }
        };
    }
        
    private Callback<CellDataFeatures<Record, String>, ObservableValue<String>> 
    getStudentNameColumnCellValueFactory() {
        return new Callback<CellDataFeatures<Record, String>, ObservableValue<String>>() { 
            @Override
            public ObservableValue<String> call(CellDataFeatures<Record, String> param) {
                String name = param.getValue().getStudentName();
                return new SimpleStringProperty(name);
            }
        };
    }
    
    private Callback<CellDataFeatures<Record, String>, ObservableValue<String>> 
    getStudentIdColumnCellValueFactory() {
        return new Callback<CellDataFeatures<Record, String>, ObservableValue<String>>() { 
            @Override
            public ObservableValue<String> call(CellDataFeatures<Record, String> param) {
                String name = param.getValue().getStudentId();
                return new SimpleStringProperty(name);
            }
        };
    }
}
