package io.github.patrnk.checkmate.test;

import io.github.patrnk.checkmate.test.exception.AnswerNotProvidedException;
import io.github.patrnk.checkmate.test.exception.BadTestInfoException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PermissiveTest implements Test {

    private final TestInfo info;
    
    @Override
    public TestInfo getInfo() {
        return info;
    }
    
    private final AnswerFormatter formatter = new AnswerFormatter();
    
    private final List<Pattern> answerKey;
    private final List<List<String>> normalAnswerKey;
        
    public PermissiveTest(TestInfo info) throws BadTestInfoException {
        if (info == null) {
            throw new IllegalArgumentException("Test info cannot be null");
        }
        answerKey = formatter.getAnswerKey(info.getDescription());
        List<List<String>> answers 
            = formatter.getSeparatedLowerCaseAnswers(info.getDescription());
        normalAnswerKey = formatter.normalize(answers);
        for (int i = 0; i < answerKey.size(); i++) {
            if (answerKey.get(i) == null) {
                throw new AnswerNotProvidedException(i + 1);
            }
        }
        
        this.info = new TestInfo(info.getName(), info.getId(), 
            info.getDescription().toLowerCase());
    }
    
    private final Integer MAX_GRADE = 2;
    private final Integer MID_GRADE = 1;
    private final Integer LOW_GRADE = 0;
    
    @Override
    public ArrayList<TestAnswer> check(ArrayList<TestAnswer> studentAnswers) {
        ArrayList<TestAnswer> answers = removeExtraneousAnswers(studentAnswers);
        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i).getAnswer();
            Matcher m = answerKey.get(i).matcher(answer);
            if (m.matches()) {
                answers.get(i).setGrade(MAX_GRADE);
            } else {
                answers.get(i).setGrade(midOrLow(i, answer));
            }
        }
        return answers;
    }
    
    private ArrayList<TestAnswer> removeExtraneousAnswers(ArrayList<TestAnswer> studentAnswers) {
        if (studentAnswers.size() > answerKey.size()) {
            ArrayList<TestAnswer> clearedStudentAnswers = new ArrayList();
            for (int i = 0; i < answerKey.size(); i++) {
                clearedStudentAnswers.add(studentAnswers.get(i));
            }
            return clearedStudentAnswers;
        }
        return studentAnswers;
    }
    
    private Integer midOrLow(int questionNumber, String studentAnswer) {
        String normalStudentAnswer = formatter.normalize(studentAnswer);
        List<String> normalAnswers = normalAnswerKey.get(questionNumber);
        for (String normalAnswer : normalAnswers) {
            if (allKeysFound(normalAnswer, normalStudentAnswer)) {
                return MID_GRADE;
            }
        }
        return LOW_GRADE;
    }
    
    private Boolean allKeysFound(String answer, String studentAnswer) {
        for (String key : studentAnswer.split("")) {
            if (answer == null || !answer.contains(key)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Integer getMaxGrade() {
        return answerKey.size() * 2;
    }
    
    private static class SerializationProxy implements Serializable {
        TestInfo info;
        
        SerializationProxy(PermissiveTest t) {
            info = t.getInfo();
        }
        
        private Object readResolve() { 
            try {
                return new PermissiveTest(this.info);
            } catch (BadTestInfoException ex) {
                return null;
            }
        }
        
        private static final long serialVersionUID = 602982433234862386L;
    }
    
    private Object writeReplace() {
        return new SerializationProxy(this);
    }
    
    private void readObject(ObjectInputStream stream) 
        throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
