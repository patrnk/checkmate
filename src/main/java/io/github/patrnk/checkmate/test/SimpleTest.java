package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.BadTestInfoException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class SimpleTest implements Test {

    private final TestInfo info;
    
    @Override
    public TestInfo getInfo() {
        return info;
    }
    
    private final AnswerFormatter formatter = new AnswerFormatter();
    
    private final List<Pattern> answerKey;
        
    SimpleTest(TestInfo info) throws BadTestInfoException {
        if (info == null) {
            throw new IllegalArgumentException("Test info cannot be null");
        }
        answerKey = formatter.getAnswerKey(info.getDescription());
        this.info = new TestInfo(info.getName(), info.getId(), 
            info.getDescription().toLowerCase());
    }

    private static final Integer MAX_GRADE = 1;
    
    private static final Integer MIN_GRADE = 0;
    
    @Override
    public ArrayList<TestAnswer> check(ArrayList<TestAnswer> studentAnswers) {
        ArrayList<TestAnswer> answers = removeExtraneousAnswers(studentAnswers);
        for (int i = 0; i < answerKey.size(); i++) {
            String answer = answers.get(i).getAnswer();
            if (answerKey.get(i).matcher(answer).matches()) {
                answers.get(i).setGrade(MAX_GRADE);
            } else {
                answers.get(i).setGrade(MIN_GRADE);
            }
        }
        return answers;
    }
    
    private ArrayList<TestAnswer> removeExtraneousAnswers(
            ArrayList<TestAnswer> studentAnswers) {
        if (studentAnswers.size() > answerKey.size()) {
            ArrayList<TestAnswer> clearedStudentAnswers = new ArrayList();
            for (int i = 0; i < answerKey.size(); i++) {
                clearedStudentAnswers.add(studentAnswers.get(i));
            }
            return clearedStudentAnswers;
        }
        return studentAnswers;
    }

    @Override
    public Integer getMaxGrade() {
        return answerKey.size();
    }
    
    private static class SerializationProxy implements Serializable {
        TestInfo info;
        
        SerializationProxy(SimpleTest t) {
            info = t.getInfo();
        }
        
        private Object readResolve() { 
            try {
                return new SimpleTest(this.info);
            } catch (BadTestInfoException ex) {
                Logger.getLogger(PermissiveTest.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        private static final long serialVersionUID = 6029824332341232386L;
    }
    
    private Object writeReplace() {
        return new SerializationProxy(this);
    }
    
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
