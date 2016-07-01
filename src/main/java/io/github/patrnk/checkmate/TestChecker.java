package io.github.patrnk.checkmate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vergeev
 */
public interface TestChecker extends Serializable {
    public List<TestAnswer> check(List<TestAnswer> studentAnswers);
}
