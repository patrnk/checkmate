package io.github.patrnk.checkmate;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines what a test looks like.
 * Different tests differ only in the way they grade student's answers.
 * Remember to create a new CheckType before subclassing (see CheckType.java).
 * @author vergeev
 */
public abstract class GeneralTest {
    
    /**
     * List of answers to the test.
     * Remember that i-th element corresponds to (i+1)-th question on the test.
     * Each element is a list of mutually exclusive answers to the question.
     *  For example, if a student can answer the first question with 1 or 2, 
     *  but not both, then answerKeys.get(0).get(0) == "1" and 
     *  answerKeys.get(0).get(1) == "2".
     */
    private List<ArrayList<String>> answerKey;
    
    /**
     * Sets a list of values that student's answers are checked against.
     * Note: i-th element corresponds to (i+1)-th question on the test, so
     * the answer to the first question can be obtained by answerKeys.get(0).
     * @param answerKey a list of answers to the questions. For further detail,
     *      refer to answerKey JavaDoc.
     */
    public void setAnswerKey(List<List<String>> answerKey) {
        this.answerKey = null;
        for (List l : answerKey) {
            this.answerKey.add(new ArrayList(l));
        }
    }
    
    public static void createTest(CheckType type) {
        System.out.println(type);
    }
    
    public abstract void check();
}