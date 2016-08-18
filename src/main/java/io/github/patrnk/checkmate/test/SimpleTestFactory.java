package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;


public class SimpleTestFactory implements TestFactory {

    private static final String TITLE = "Простой";
    
    private static final String SUMMARY = "За каждый полный правильный ответ "
        + "учащемуся присуждается 1 балл.";
    
    
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
        return new SimpleTest(test);
    }
    
}
