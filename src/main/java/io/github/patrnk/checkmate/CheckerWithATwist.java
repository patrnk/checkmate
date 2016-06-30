package io.github.patrnk.checkmate;

import java.util.Map;

/**
 * It's a singleton.
 * @author vergeev
 */
public final class CheckerWithATwist implements TestChecker {

    private static CheckerWithATwist instance = null;
    
    private CheckerWithATwist() {}
    
    public static CheckerWithATwist getInstance() {
        if (instance == null) {
            instance = new CheckerWithATwist();
        }
        return instance;
    }
    
    @Override
    public TestResult check(Map<Integer, String> studentAnswers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
