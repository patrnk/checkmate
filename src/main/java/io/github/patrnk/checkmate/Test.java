package io.github.patrnk.checkmate;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vergeev
 */
public interface Test extends Serializable {
    /**
     * Grades each student answer individually.
     * @param studentAnswers list of answers where i-th element is (i+1)-th answer.
     * @return same <code>studentAnswers</code> but with grades
     */
    public List<TestAnswer> check(List<TestAnswer> studentAnswers);
}
