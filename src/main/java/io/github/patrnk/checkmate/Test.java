package io.github.patrnk.checkmate;

import static io.github.patrnk.checkmate.MainApp.database;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 * @author vergeev
 */
public final class Test implements Serializable {
    
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
     * This is how students will identify a particular test.
     */
    private Long id;
    
    public Long getId() {
        return id;
    }
    
    private String description;
    
    public String getDescription() {
        return description;
    }
    
    private TestChecker checker;
    
    public TestChecker getChecker() {
        return checker;
    }
    
    private Test() {}
    
    public static Test createTest(String name, Long id, 
        String testDescription, TestChecker checker) {
        Test t = new Test();
        t.name = name;
        t.id = id;
        t.description = testDescription;
        return t;
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
    
    // TODO serialize TestChecker too
    private static class SerializationProxy implements Serializable {
        String name;
        Long id;
        CheckType type;
        String testDescription;
        
        SerializationProxy(Test t) {
            this.name = t.getName();
            this.id = t.getId();
            this.type.fromString(t.getClass().getName());
            this.testDescription = t.getDescription();
        }
        
        private Object readResolve() { 
            return Test.createTest(name, id, testDescription, null);
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

//    // TODO update answerKey description
//    /**
//     * List of regex which are used to check the answers.
//     * An answer can be expected to be in one of three forms:
//     * <br> 1) <code>^[abc]$</code> — used when there are several mutually
//     *      exclusive answers. Here, "a", "b" or "c" are all accepted answers.
//     * <br> 2) <code>^word$</code> — used when an answer is an ordered 
//     *      sequence of symbols. Here, "word" is only accepted answer.
//     * <br> 3) <code>^(?=.*a)(?=.*b)[ab]{2}$</code> — used when an answer
//     *      is a unordered sequence of symbols. Here, both "ab" and "ba" are accepted.
//     */
//    private final List<Pattern> answerKey = new ArrayList();