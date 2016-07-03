package io.github.patrnk.checkmate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;


public class MainApp extends Application {
        // TODO: code the email reader part

    @Override
    public void start(Stage stage) throws Exception {
        openConnection();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        //stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }
    
    // It's easier to remember the name. Shoudn't be changing a lot anyway.
    //private final static String GLOBAL_TABLE_NAME = "global";
    
    /**
     * Connection to the application's global database.
     */
    public static Connection database = null;
    
    /**
     * Creates or opens a SQLite database.
     * Connection can be accessed through MainApp.database.
    */
    private void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            database = DriverManager.getConnection("jdbc:sqlite:mockup.db");
            
            ResultSet tables = database.getMetaData().getTables(null, null, "global", null);
            if (!tables.next()) {   
                String createQuery = "CREATE TABLE global (" +
                        "report_id NVARCHAR(255), " +
                        "test_id INT, " +
                        "student_id NVARCHAR(255), " + // it's supposed to be an email
                        "details_id INT, " +
                        "grade INT" +
                        ")";
                Statement createGlobalTable = database.createStatement();
                createGlobalTable.executeUpdate(createQuery);
                createGlobalTable.close();
            }
        } catch (Exception e) {
            CmUtils.printException(e);
        } 
    }
    
    /**
     * Creates essential tables if needed.
     * Created tables include "global" and "tests". 
     */
    private void createTables() {
        try {
            createGlobalTable();
            TestInfo.createTestsTable();
        } catch (Exception e) {
            CmUtils.printException(e);
        } 
    }
    
    /**
     * Creates "global" table if needed.
     * Helper method for createTables().
     */
    private void createGlobalTable() throws Exception {
        ResultSet tables = database.getMetaData().getTables(null, null, "global", null);
        if (!tables.next()) {   
            String createQuery = "CREATE TABLE global (" +
                    "report_id nvarchar(255), " +
                    "test_id int, " +
                    "student_id nvarchar(255), " + // it's supposed to be an email
                    "details_id int, " +
                    "grade int" +
                    ")";
            Statement createTable = database.createStatement();
            createTable.executeUpdate(createQuery);
            createTable.close();
        }
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
            CmUtils.printException(e);
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
            CmUtils.printException(e);
        }
    }
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
