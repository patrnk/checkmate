package io.github.patrnk.checkmate;

import java.util.Map;

/**
 *
 * @author vergeev
 */
public interface TestChecker {
    public TestResult check(Map<Integer, String> studentAnswers);
}
