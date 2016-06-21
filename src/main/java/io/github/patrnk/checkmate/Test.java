package io.github.patrnk.checkmate;

import static io.github.patrnk.checkmate.MainApp.database;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Gets set on calling createTest().
     */
    private String name;
    
    public String getName() {
        return name;
    }
    
    private int id;
    
    /**
     * List of answers to the test.
     * <br>
     * Remember that i-th element corresponds to (i+1)-th question on the test.
     * Each element is a list of mutually exclusive answers to the question.
     * <br>
     * For example, if a student can answer the first question with 1 or 2, 
     * but not both, then <code>answerKeys.get(0).get(0) == "1"</code> and 
     * <code>answerKeys.get(0).get(1) == "2"</code>.
     */
        private List<List<String>> answerKey = new ArrayList();
 
    /**
     * Sets a list of values that student's answers are checked against.
     * @param answerKey a list of answers to the questions. For further detail,
     *      refer to answerKey JavaDoc.
     */
    private void setAnswerKey(List<List<String>> answerKey) {
        this.answerKey = null;
        for (List l : answerKey) {
            this.answerKey.add(new ArrayList(l));
        }
    }

//    TODO: Enforce usage of .createTest
//    public Test() {
//        throw new UnsopportedOperationException();
//    }
    
    /**
     * 
     * @param name
     * @param type
     * @param content
     * @return
     * @throws ParseException 
     */
    public static Test createTest(String name, CheckType type, String content) 
            throws ParseException {
        return null;
    }
    
    /**
     * A symbol that separates a question number and an answer.
     */
    private static final String ANSWER_SEPARATOR_REGEX = "\\)";
    
    /**
     * Parses the data for answerKey.
     * @throws ParseException if a line is formatted incorrectly.
     * @throws IllegalArgumentException if there is more than 1024 lines in the
     *      description or if one of the question numbers is greater than 256.
     */
    private void initializeAnswerKey(String testDescription) 
            throws ParseException, IllegalArgumentException{
        testDescription = testDescription.trim();
        String[] lines = testDescription.split("\n");
        if (lines.length > 1024) {
            throw new IllegalArgumentException("Too many lines in "
                    + "the test description");
        }
        
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
            if (!lines[i].matches(".+" + ANSWER_SEPARATOR_REGEX + ".+")) {
                throw new ParseException("The line formatted incorrectly. ", i);
            }
            
            Integer problemNumber = -1;
            try {
                problemNumber = Integer.valueOf(
                        lines[i].split(ANSWER_SEPARATOR_REGEX)[0].trim());
                if (problemNumber < 0 || problemNumber > 256) {
                    throw new IndexOutOfBoundsException();
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new ParseException("The question number is incorrect.", i);
            }
            assert(problemNumber > 0);
            
            String answer = lines[i].split(ANSWER_SEPARATOR_REGEX)[1].trim();
            
            if (problemNumber >= answerKey.size()) {
                for (int j = answerKey.size(); j <= problemNumber; j++) {
                    answerKey.add(new ArrayList());
                }
            }
            assert (problemNumber < answerKey.size());
            
            this.answerKey.get(problemNumber).add(answer);
        }
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
    
//    private static int setId() {
//        
//    }
}