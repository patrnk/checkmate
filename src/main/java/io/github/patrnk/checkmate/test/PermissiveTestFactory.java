package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;


public final class PermissiveTestFactory implements TestFactory {
    
    private static final String TITLE = "Нестандартный";
    private static final String SUMMARY = "Каждый полный правильный ответ "
        + "стоит 2 балла. Если учащийся отвечает на вопрос без ошибки, но не "
        + "все верные ответы находит, то ему присуждается 1 балл. Если же он "
        + "делает ошибку в ответе, то этот вопрос стоит 0 баллов.";
    
    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getSummary() {
        return SUMMARY;
    }
    
    @Override
    public Test getTest(TestInfo t) throws BadTestInfoException {
        return new PermissiveTest(t);
    }
}
