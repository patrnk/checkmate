package io.github.patrnk.checkmate;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PermissiveTest implements Test {

    private final TestInfo info;
    
    public TestInfo getInfo() {
        return info;
    }
    
    private final TestDescriptionRegexFormatter 
        formatter = new TestDescriptionRegexFormatter();
    
    private final List<Pattern> answerKey;
    
    public PermissiveTest(TestInfo info) 
        throws MalformedTestDescriptionException, AnswerNotProvidedException {
        if (info == null) {
            throw new IllegalArgumentException("Test info cannot be null");
        }
        try {
            answerKey = formatter.formRegexList(info.getDescription());
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
    
    @Override
    public List<TestAnswer> check(List<TestAnswer> studentAnswers) {
        throw new UnsupportedOperationException();
//        for (int i = 0; i < studentAnswers.size(); i++) {
//            Matcher m = answerKey.get(i).matcher(studentAnswers.get(i).getAnswer());
//            if (m.matches() == true) {
//                studentAnswers.get(i).setGrade(MAX_GRADE);
//            } else {
//                studentAnswers.get(i).setGrade(mid_or_low());
//            }
//        }
//    private final Integer MAX_GRADE = 2;
//    private final Integer MID_GRADE = 1;
//    private final Integer LOW_GRADE = 0;
    }
    
    private static final long serialVersionUID = 602982433234862386L;
    //TODO: implement safe readObject()
}
