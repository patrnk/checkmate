package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;


public class StandardTestFactory implements TestFactory {

    private static final String TITLE = "Стандартный";
    
    private static final String SUMMARY = "За правильные ответы, "
        + "помеченные звездочкой в начале, присуждается 2 балла. "
        + "За другие — 1 балл."; 
    
    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getSummary() {
        return SUMMARY;
    }

    @Override
    public Test getTest(TestInfo test) throws BadTestInfoException {
        return new StandardTest(test);
    }
    
}
