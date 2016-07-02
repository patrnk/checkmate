package io.github.patrnk.checkmate;

import java.util.List;


public class PermissiveTest implements Test {

    private final TestInfo info;
    
    public TestInfo getInfo() {
        return info;
    }
    
    public PermissiveTest(TestInfo info) {
        if (info == null) {
            throw new IllegalArgumentException("Test info cannot be null");
        }
        this.info = info;
    }
    
    @Override
    public List<TestAnswer> check(List<TestAnswer> t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private static final long serialVersionUID = 602982433234862386L;
}
