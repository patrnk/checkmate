package io.github.patrnk.checkmate;

import java.util.ArrayList;
import java.util.List;


public abstract class GeneralTest {
    
    List<ArrayList<String>> answerKeys;
    
    /**
     * Sets a list of values that student's answers are checked against.
     * Note: i-th element corresponds to (i+1)-th question on the test, so
     * the answer to the first question can be obtained via answerKeys.get(0).
     * @param answerKeys every list in the answerKeys list carries 
     * all possible mutually exclusive answers to a question.
     * For example, if you can answer the first question with 1 or 2, 
     * but not both, then answerKeys.get(0).get(0) == "1" and 
     * answerKeys.get(0).get(1) == "2". 
     */
    public void setAnswerKeys(List<List<String>> answerKeys) {
        this.answerKeys = null;
        for (List l : answerKeys) {
            this.answerKeys.add(new ArrayList(l));
        }
    }
    
    public void createTest(CheckType type) {
        switch (type) {
            
            default: throw new UnsupportedOperationException("This CheckType "
                    + "is not known to createTest method.");
        }
    }
    
    public abstract void check();
}