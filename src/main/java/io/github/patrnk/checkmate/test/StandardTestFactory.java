package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;


public class StandardTestFactory implements TestFactory {

    private static final String TITLE = "Стандартный";
    
    private static final String SUMMARY = "За правильный ответ, состоящий из "
        + "одного символа, учащемуся присуждается 1 балл. За правильный ответ, "
        + "состоящий из нескольких, — 2 балла.";
    
    
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
