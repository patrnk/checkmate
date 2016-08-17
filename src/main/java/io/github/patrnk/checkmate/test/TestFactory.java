package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;

public interface TestFactory {
    
    /**
     * Returns the title of the category of tests.
     * It's used to distinguish between different ones.
     */
    public String getTitle();

    /**
     * Returns the summary of the category of tests.
     * Summary describes how student answers are graded.
     */    
    public String getSummary();
    
    public Test getTest(TestInfo test) throws BadTestInfoException;
}
