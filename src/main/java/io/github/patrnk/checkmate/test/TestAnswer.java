package io.github.patrnk.checkmate.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * The way of storing student's answers.
 * Each instance contains student's answer itself and the grade for the answer.
 * It's guaranteed that the answer is not null.
 * @author vergeev
 */
public class TestAnswer implements Serializable {
    
    private final String answer;
    
    /**
     * Gets student's answer.
     * Never null.
     */
    public String getAnswer() {
        return answer;
    }
    
    private Integer grade = null;
    
    /**
     * Sets the grade for student's answer.
     * Once set, stays immutable.
     * @param grade can only be set once.
     */
    public void setGrade(Integer grade) {
        if (this.grade == null) {
            this.grade = grade;
        } else {
            throw new IllegalStateException("Cannot reset the grade.");
        }
    }
    
    /**
     * The grade for student's answer.
     * Can be null.
     */
    public Integer getGrade() {
        return grade;
    }
    
    public TestAnswer(String answer) {
        this.answer = answer;
    }

    public TestAnswer(String answer, Integer grade) {
        this.answer = answer;
        this.grade = grade;
    }
    
    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (answer == null) {
            throw new AssertionError("Answer cannot be null.");
        }
    }
}