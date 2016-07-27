package io.github.patrnk.checkmate;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ViewSceneController implements Initializable {

    @FXML
    private Label headTextLabel;
    
    @FXML
    private TextArea bodyTextArea;
        
    public void setText(String headText, String bodyText) {
        headTextLabel.setText(headText);
        bodyTextArea.setText(bodyText);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
