package io.github.patrnk.checkmate;

/**
 *
 * @author vergeev
 */
public interface TestFactory {
    public String getTitle();
    public String getSummary();
    public Test getTest(TestInfo test) throws BadTestInfoException;
}
