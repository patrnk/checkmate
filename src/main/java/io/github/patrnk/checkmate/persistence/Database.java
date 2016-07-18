package io.github.patrnk.checkmate.persistence;

import io.github.patrnk.checkmate.CmUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


final class Database {
    
    private static Connection connection = null;
    
    private static final Integer MAX_STRING_LENGTH = 255;
    
    private static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mockup.db");
            createTablesIfNeeded();
        } catch (ClassNotFoundException | SQLException ex) {
            CmUtils.printExceptionAndExit(ex);
        }
        return connection;
    }
    
    private static void createTablesIfNeeded() throws SQLException {
        createGlobalTableIfNeeded();
    }
    
    private static void createGlobalTableIfNeeded() throws SQLException {
        ResultSet tables = 
            connection.getMetaData().getTables(null, null, "global", null);
        if (!tables.next()) {   
            String createQuery = "CREATE TABLE global " +
                "(test_id int, " +
                "student_name nvarchar(" + MAX_STRING_LENGTH.toString() + ")," +
                "student_id nvarchar(" + MAX_STRING_LENGTH.toString() + "), " +
                "answer_file nvarchar(" + MAX_STRING_LENGTH.toString() + "))";
            try (Statement createTable = connection.createStatement()) {
                createTable.executeUpdate(createQuery);
            }
        }
    }
    
    /**
     * Sets new record of the test results in the database.
     * @param testId id of the test that the student has taken.
     * @param studentName name of the student
     * @param studentId id of the student
     * @param answerFileName name of a file where student answers and grades
     *      are stored
     * @throws SQLException in case the method is broken and needs to be rewritten
     */
    public static void addRecord(Record record) throws SQLException {
        
        checkStringLength(record.getStudentName(), record.getStudentId(), 
            record.getAnswerFileName());
        
        String insertSql = "INSERT INTO global " +
                "(test_id, student_name, student_id, answer_file) VALUES " +
                "(      ?,            ?,          ?,           ?)";
        PreparedStatement insert = 
            Database.getConnection().prepareStatement(insertSql);
        
        insert.setInt(1, record.getTestId());
        insert.setString(2, record.getStudentName());
        insert.setString(3, record.getStudentId());
        insert.setString(4, record.getAnswerFileName());
        insert.executeUpdate();
    }
    
    public static List<Record> fetchRecords() throws SQLException {
        List<Record> records = new ArrayList();
        String selectSql = "SELECT * FROM global";
        Statement select = Database.getConnection().createStatement();
        ResultSet rawRecords = select.executeQuery(selectSql);
        while (rawRecords.next()) {
            Integer testId = rawRecords.getInt("test_id");
            String studentName = rawRecords.getNString("student_name");
            String studentId = rawRecords.getNString("student_id");
            String answerFilename = rawRecords.getNString("answer_file");
            try {
                Record record = new Record(testId, studentName, studentId, answerFilename);
                records.add(record);
            } catch (BadStudentNameException | BadStudentIdException ex) {
                // Is this how you log the thing? 
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return records;
    }
     
    private static void checkStringLength(String... strings) {
        for (String s : strings) {
            if (s.length() > MAX_STRING_LENGTH) { // TODO: get rid of 255
                throw new IllegalArgumentException("Every string in "
                    + " the database must be " + MAX_STRING_LENGTH + 
                    " symbols long of shorter.");
            }
        }
    }
    
    private Database() {
        throw new AssertionError("You cannot instantiate the Database class");
    }
}
