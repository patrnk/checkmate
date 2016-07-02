package io.github.patrnk.checkmate;

import java.util.List;


public class PermissiveTest implements TestChecker {

    private final Test test;
    
    public Test getTest() {
        return test;
    }
    
    public PermissiveTest(Test test) {
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
