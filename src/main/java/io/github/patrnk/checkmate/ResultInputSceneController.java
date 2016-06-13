/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.patrnk.checkmate;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

/**
 * FXML Controller class
 *
 * @author vergeev
 */
public class ResultInputSceneController implements Initializable {

    @FXML
    private ComboBox nameBox;
    @FXML
    private ComboBox testIdBox;
    @FXML
    private ComboBox reportIdBox;
    
    public void checkButtonClicked(ActionEvent event) {
//        Statement insert = null;
//        String insertSql = "INSERT INTO global " +
//                "(student_id, test_id, report_id, details_id, grade) " +
//                "VALUES (" + nameBox.getEditor().getText() + ", " +
//                testIdBox.getEditor().getText() + ", " +
//                reportIdBox.getEditor().getText() + ", " +
//                "-1" + ", " +
//                "-1" +
//                ")";
        PreparedStatement insert = null;
        String insertSql = "INSERT INTO global " +
                "(student_id, test_id, report_id, details_id, grade) VALUES " +
                "(         ?,       ?,         ?,          ?,     ?)";
        try {
            insert = MainApp.database.prepareStatement(insertSql);
            insert.setString(1, nameBox.getEditor().getText());
            insert.setInt(2, Integer.parseInt(testIdBox.getEditor().getText()));
            insert.setString(3, reportIdBox.getEditor().getText());
            insert.setInt(4, -1); // a placeholder, fill in with real
            insert.setInt(5, -1); // results when they become available
            insert.executeUpdate();
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        nameBox.getScene().getWindow().hide();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: fill in the comboboxes with data from db
    }
    
}
