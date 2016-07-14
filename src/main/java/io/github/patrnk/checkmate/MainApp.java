package io.github.patrnk.checkmate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import static javafx.application.Application.launch;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        //stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }
    
    // --------------------- 
    // SAMPLES (delete when not needed)
    // ---------------------
    public static void dbExample() {
        // may be useful: http://www.javafxapps.in/tutorial/Persisting-TableView-datas-in-Database.html
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC"); // initialize JDBC driver class
            conn = DriverManager.getConnection("jdbc:sqlite:test.db"); // creates or opens a db
            System.out.println("Database was opened successfully");
            
            stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM company");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String  address = rs.getString("address");
                float salary = rs.getFloat("salary");
                System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "AGE = " + age );
                System.out.println( "ADDRESS = " + address );
                System.out.println( "SALARY = " + salary );
                System.out.println();
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            CmUtils.printExceptionAndExit(e);
        }
        System.out.println("Success!");
    }
    // Mailchecker. Adjuct according to the needs.
    public static void check(String host, String user, String password) {
        System.out.println("Trying to get the mail");    
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.yandex.com", "chemistry.check@yandex.com", "peanutbutter");            
            
            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                //System.out.println("Text: " + message.getContent().toString());
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            CmUtils.printExceptionAndExit(e);
        }
    }
    
    // Source is taken from here: 
    // http://blog.ngopal.com.np/2011/10/19/dyanmic-tableview-data-from-database/comment-page-1/
//    private void buildData() {
//        ObservableList<ObservableList> data = FXCollections.observableArrayList();
//        try {
//            String sql = "SELECT * from global";
//            ResultSet rs = MainApp.database.createStatement().executeQuery(sql);
//            // Add table columns
//            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
//                final int j = i;
//                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(j + 1));
//                col.setCellValueFactory(
//                new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
//                    @Override
//                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
//                        return new SimpleStringProperty(param.getValue().get(j).toString());
//                    }       
//                });
//                globalTable.getColumns().addAll(col);
//            }
//            // Add data to observable list "data"
//            while (rs.next()) {
//                ObservableList<String> row = FXCollections.observableArrayList();
//                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//                    row.add(rs.getString(i));
//                }
//                data.add(row);
//            }
//            // Add data to TableView
//            globalTable.setItems(data);
//        } catch (Exception e) {
//            CmUtils.printExceptionAndExit(e);
//        }
//    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
