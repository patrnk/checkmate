package io.github.patrnk.checkmate;

import static io.github.patrnk.checkmate.MainApp.database;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// TODO: rethink the design of the class.
/**
 * Defines what a test looks like.
 * Different tests differ only in the way they grade student's answers.
 * Remember to create a new CheckType before subclassing (see CheckType.java).
 * @author vergeev
 */
public abstract class Test implements Serializable {
    
    private final TestDescriptionFormatter 
        formatter = new TestDescriptionFormatter();
    
    public final int MAX_NAME_LENGTH = 255;
    
    /**
     * Test name that's set by and displayed to the user.
     * Cannot be exported. Cannot be empty or longer than MAX_NAME_LENGTH. 
     * Gets set on calling createTest().
     */
    private String name;
    
    public String getName() {
        return name;
    }
    
    /**
     * Used for convenience of the students.
     */
    private Long id;
    
    public Long getId() {
        return id;
    }
    
    // TODO update answerKey description
    /**
     * List of regex which are used to check the answers.
     * An answer can be expected to be in one of three forms:
     * <br> 1) <code>^[abc]$</code> — used when there are several mutually
     *      exclusive answers. Here, "a", "b" or "c" are all accepted answers.
     * <br> 2) <code>^word$</code> — used when an answer is an ordered 
     *      sequence of symbols. Here, "word" is only accepted answer.
     * <br> 3) <code>^(?=.*a)(?=.*b)[ab]{2}$</code> — used when an answer
     *      is a unordered sequence of symbols. Here, both "ab" and "ba" are accepted.
     */
    private final List<Pattern> answerKey = new ArrayList();

    private String testDescription;
    
    public String getTestDescription() {
        return testDescription;
    }
    
    /**
     * TODO write createTest javadoc, read item 17
     */
    public static Test createTest(String name, Long id, 
        CheckType type, String testDescription) {
        return null;
    }

    public abstract void check();
    
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
        CheckType type;
        String testDescription;
        
        SerializationProxy(Test t) {
            this.name = t.getName();
            this.id = t.getId();
            this.type.fromString(t.getClass().getName());
            this.testDescription = t.getTestDescription();
        }
        
        private Object readResolve() { 
            return Test.createTest(name, id, type, testDescription);
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