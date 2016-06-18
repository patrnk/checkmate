package io.github.patrnk.checkmate;

import static io.github.patrnk.checkmate.MainApp.database;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines what a test looks like.
 * Different tests differ only in the way they grade student's answers.
 * Remember to create a new CheckType before subclassing (see CheckType.java).
 * @author vergeev
 */
public abstract class Test {
    
    public final int MAX_NAME_LENGTH = 255;
    
    /**
     * Test name that's set by and displayed to the user.
     * Cannot be exported. Cannot be empty or longer than MAX_NAME_LENGTH. 
     */
    private String name;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * List of answers to the test.
     * <br>
     * Remember that i-th element corresponds to (i+1)-th question on the test.
     * Each element is a list of mutually exclusive answers to the question.
     * <br>
     * For example, if a student can answer the first question with 1 or 2, 
     * but not both, then answerKeys.get(0).get(0) == "1" and 
     * answerKeys.get(0).get(1) == "2".
     */
    private List<ArrayList<String>> answerKey = null;
    
    /**
     * Sets a list of values that student's answers are checked against.
     * @param answerKey a list of answers to the questions. For further detail,
     *      refer to answerKey JavaDoc.
     */
    public void setAnswerKey(List<List<String>> answerKey) {
        this.answerKey = null;
        for (List l : answerKey) {
            this.answerKey.add(new ArrayList(l));
        }
    }
    
    public Test createTest(CheckType type) {
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
    
}