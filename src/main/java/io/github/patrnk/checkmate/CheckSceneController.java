package io.github.patrnk.checkmate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author vergeev
 */
public class CheckSceneController implements Initializable {
    
    @FXML
    private AnchorPane anchor;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void openResultInputWindow(ActionEvent event) {
        // TODO: improve window transitioning
        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/ResultInputScene.fxml"));
            root = (Parent)loader.load();
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));
            stage.onHiddenProperty().setValue(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ((Stage)anchor.getScene().getWindow()).hide();
                }
            });
            stage.show();
            anchor.setDisable(true);
        } catch (IOException e) {
            CmUtils.printException(e);
        }
    }
}
