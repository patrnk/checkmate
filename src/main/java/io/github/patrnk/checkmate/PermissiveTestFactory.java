package io.github.patrnk.checkmate;


public final class PermissiveTestFactory implements TestFactory {
    private static final String TITLE = "Нестандартный";
    private static final String SUMMARY = "...";
    
    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getSummary() {
        return SUMMARY;
    }
    
    @Override
    public Test getTest(TestInfo t) 
        throws MalformedTestDescriptionException, AnswerNotProvidedException {
        return new PermissiveTest(t);
    }
}
