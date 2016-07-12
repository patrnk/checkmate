package io.github.patrnk.checkmate;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
    private TableView globalTable;
    
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
            CmUtils.printException(e);
        }
    }
    
    @FXML
    private void globalTableClicked() {
        System.out.println(String.valueOf(globalTable.getSelectionModel().getSelectedIndex()));
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
            CmUtils.printException(e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildData();
        populateTestsTable();
    }
    
    // Source is taken from here: 
    // http://blog.ngopal.com.np/2011/10/19/dyanmic-tableview-data-from-database/comment-page-1/
    private void buildData() {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from global";
            ResultSet rs = MainApp.database.createStatement().executeQuery(sql);
            // Add table columns
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(j + 1));
                col.setCellValueFactory(
                new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }       
                });
                globalTable.getColumns().addAll(col);
            }
            // Add data to observable list "data"
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            // Add data to TableView
            globalTable.setItems(data);
        } catch (Exception e) {
            CmUtils.printException(e);
        }
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
