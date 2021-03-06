package io.github.patrnk.checkmate.persistence;

import java.io.IOException;
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
    
    private static final StoredData DATABASE = new StoredData("Database", "db");
    
    private static Connection connection = null;
    
    private static final Integer MAX_STRING_LENGTH = 255;
    
    private static Connection getConnection() throws IOException {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String dir = DATABASE.getFilePath("records");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dir);
                createTablesIfNeeded();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }
    
    private static void createTablesIfNeeded() throws SQLException {
        createTestResultTableIfNeeded();
    }
    
    private static void createTestResultTableIfNeeded() throws SQLException {
        ResultSet tables = 
            connection.getMetaData().getTables(null, null, "testResult", null);
        if (!tables.next()) {   
            String createQuery = "CREATE TABLE testResult " +
                "(test_id int, " +
                "student_name varchar(" + MAX_STRING_LENGTH.toString() + ")," +
                "student_id varchar(" + MAX_STRING_LENGTH.toString() + "), " +
                "answer_file varchar(" + MAX_STRING_LENGTH.toString() + "))";
            try (Statement createTable = connection.createStatement()) {
                createTable.executeUpdate(createQuery);
            }
        }
    }
    
    /**
     * Sets new record of the test results in the database.
     * @throws SQLException in case the method is broken and needs to be rewritten.
     * @throws IOException if the db file couldn't be opened.
     */
    public static void addRecord(Record record) throws SQLException, IOException {
        checkStringLength(record.getStudentName(), record.getStudentId(), 
            record.getResultFilepath());
        String insertSql = "INSERT INTO testResult " +
                "(test_id, student_name, student_id, answer_file) VALUES " +
                "(      ?,            ?,          ?,           ?);";
        PreparedStatement insert = 
            Database.getConnection().prepareStatement(insertSql);
        try {
            insert.setInt(1, record.getTestId());
            insert.setString(2, record.getStudentName());
            insert.setString(3, record.getStudentId());
            insert.setString(4, record.getResultFilepath());
            insert.executeUpdate();
        } finally {
            insert.close();
        }
    }
    
    public static List<Record> fetchRecords() throws SQLException, IOException {
        List<Record> records = new ArrayList();
        String selectSql = "SELECT * FROM testResult;";
        Statement select = Database.getConnection().createStatement();
        try { 
            ResultSet rawRecords = select.executeQuery(selectSql);
            while (rawRecords.next()) {
                Integer testId = rawRecords.getInt("test_id");
                String studentName = rawRecords.getString("student_name");
                String studentId = rawRecords.getString("student_id");
                String resultFilePath = rawRecords.getString("answer_file");
                try {
                    Record record = new Record(testId, studentName, studentId, resultFilePath);
                    records.add(record);
                } catch (BadStudentNameException | BadStudentIdException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            select.close();
        }
        return records;
    }
     
    private static void checkStringLength(String... strings) {
        for (String s : strings) {
            if (s.length() > MAX_STRING_LENGTH) {
                throw new IllegalArgumentException("Every string in "
                    + " the database must be " + MAX_STRING_LENGTH + 
                    " symbols long of shorter.");
            }
        }
    }
    
    /**
     * Deletes the row with the record.
     * @param resultFilePath used as an identifier of the record.
     * @throws SQLException if something went wrong with the SQL request.
     * @throws IOException if the db file couldn't be opened.
     */
    public static void deleteRecord(String resultFilePath) throws SQLException, IOException {
        String deleteSql = "DELETE FROM testResult WHERE answer_file= ? ;";
        try (PreparedStatement delete = 
            Database.getConnection().prepareStatement(deleteSql)) {
            delete.setString(1, resultFilePath);
            delete.executeUpdate();
        }
    }
    
    private Database() {
        throw new AssertionError("You cannot instantiate the Database class");
    }
}
