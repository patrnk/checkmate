package io.github.patrnk.checkmate;

import java.util.List;


public class PermissiveTest implements TestChecker {

    private final TestInfo test;
    
    public TestInfo getTest() {
        return test;
    }
    
    public PermissiveTest(TestInfo test) {
        if (test == null) {
            throw new IllegalArgumentException("Test cannot be null");
        }
        this.test = test;
    }
    
    @Override
    public List<TestAnswer> check(List<TestAnswer> t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
