package io.github.patrnk.checkmate.test;

import java.io.Serializable;
import java.util.ArrayList;

public interface Test extends Serializable {
    
    public TestInfo getInfo();
    
    /**
     * Grades each student answer individually.
     * Ignores an answer if there's no key for it.
     * @param studentAnswers list of answers where i-th element is (i+1)-th answer.
     * @return list of TestAnswer with the same answers and grades. All answers
     *      with number greater than that provided in answer key are dropped.
     */
    public ArrayList<TestAnswer> check(ArrayList<TestAnswer> studentAnswers);
    
    /**
     * Get max possible amount of points a student can get for the test.
     */
    public Integer getMaxGrade();
}
