package io.github.patrnk.checkmate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.info = info;
        try {
            answerKey = formatter.formRegexList(this.info.getDescription());
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
    }
    
    @Override
    public List<TestAnswer> check(List<TestAnswer> t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private static final long serialVersionUID = 602982433234862386L;
}
