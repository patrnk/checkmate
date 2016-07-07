package io.github.patrnk.checkmate;

import static io.github.patrnk.checkmate.MainApp.database;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author vergeev
 */
public final class TestInfo implements Serializable {
    
    public static final int MAX_NAME_LENGTH = 255;
    
    /**
     * Test name that's set by and displayed to the user.
     * Cannot be null, empty or longer than MAX_NAME_LENGTH.
     */
    private final String name;
    
    public String getName() {
        return name;
    }
    
    
    private void checkName(String name) throws BadTestNameException {
        if (name == null) {
            throw new BadTestNameException("The name cannot be  null");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BadTestNameException("The name is too long: " + 
                name.length() + " > " + MAX_NAME_LENGTH);
        }
        if (name.length() == 0) {
            throw new BadTestNameException("The name cannot be "
                + "an empty string");
        }
    }
    
    /**
     * This is how students will identify a particular test.
     * This is a client's responsibility to ensure the id is unique.
     */
    private final Long id;
    
    public Long getId() {
        return id;
    }
    
    private final String description;
    
    public String getDescription() {
        return description;
    }
    
    public TestInfo(String name, Long id, String testDescription)
        throws BadTestNameException {
        checkName(name);
        this.name = name;
        this.id = id;
        this.description = testDescription;
    }
    
    /**
     * Creates "tests" table in global database if needed.
     */
    public static void createTestsTable() throws Exception {
        ResultSet tables = database.getMetaData().getTables(null, null, "tests", null);
        if (!tables.next()) {   
            String createQuery = "CREATE TABLE tests (" +
                    "test_id int PRIMARY KEY, " +
                    "name nvarchar(255) " +
                    ")";
            Statement createTable = database.createStatement();
            createTable.executeUpdate(createQuery);
            createTable.close();
        }
    }
    
    private static class SerializationProxy implements Serializable {
        String name;
        Long id;
        String testDescription;
        
        SerializationProxy(TestInfo t) {
            this.name = t.getName();
            this.id = t.getId();
            this.testDescription = t.getDescription();
        }
        
        private Object readResolve() { 
            try {
                return new TestInfo(name, id, testDescription);
            } catch (BadTestNameException ex) {
                return null;
            }
        }
        
        private static final long serialVersionUID = 402982438234852385L;
    }
    
    private Object writeReplace() {
        return new SerializationProxy(this);
    }
    
    private void readObject(ObjectInputStream stream) 
        throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}