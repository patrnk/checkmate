package io.github.patrnk.checkmate;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PermissiveTest implements Test {

    private final TestInfo info;
    
    public TestInfo getInfo() {
        return info;
    }
    
    private final TestDescriptionRegexFormatter 
        formatter = new TestDescriptionRegexFormatter();
    private final AnswerNormalizer normalizer = new AnswerNormalizer();
    
    private final List<Pattern> answerKey;
    private final List<List<String>> normalAnswerKey;
        
    public PermissiveTest(TestInfo info) throws BadTestInfoException {
        if (info == null) {
            throw new IllegalArgumentException("Test info cannot be null");
        }
        try {
            answerKey = formatter.formRegexList(info.getDescription());
            normalAnswerKey = normalizer.getNormalAnswers(info.getDescription());
        } catch (ParseException ex) {
            throw new MalformedTestDescriptionException("The line " 
                + ex.getErrorOffset() + " formatted incorrectly. Expecting "
                + "\"number)answer\" format.", 
                ex.getErrorOffset());
        }
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
    public List<TestAnswer> check(List<TestAnswer> studentAnswers) {
        for (int i = 0; i < studentAnswers.size(); i++) {
            String answer = studentAnswers.get(i).getAnswer();
            Matcher m = answerKey.get(i).matcher(answer);
            if (m.matches()) {
                studentAnswers.get(i).setGrade(MAX_GRADE);
            } else {
                studentAnswers.get(i).setGrade(midOrLow(i, answer));
            }
        }
        return studentAnswers;
    }
    
    private Integer midOrLow(int questionNumber, String studentAnswer) {
        String normalStudentAnswer = normalizer.normalize(studentAnswer);
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
            if (!answer.contains(key)) {
                return false;
            }
        }
        return true;
    }
    
    private static class SerializationProxy implements Serializable {
        TestInfo info;
        
        SerializationProxy(PermissiveTest t) {
            info = t.getInfo();
        }
        
        private Object readResolve() { 
            try {
                return new PermissiveTest(this.info);
            } catch (MalformedTestDescriptionException | AnswerNotProvidedException ex) {
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
