package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;

public interface TestFactory {
    
    public String getTitle();
    
    public String getSummary();
    
    public Test getTest(TestInfo test) throws BadTestInfoException;
}
