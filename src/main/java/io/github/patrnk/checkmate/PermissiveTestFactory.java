package io.github.patrnk.checkmate;


public class PermissiveTestFactory {
    private static final String TITILE = "Дозволительный";
    private static final String SUMMARY = "...";
    
    public static PermissiveTest getPermissiveTest(TestInfo t) {
        return new PermissiveTest(t);
    }
}
