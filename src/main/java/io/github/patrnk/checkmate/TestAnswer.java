package io.github.patrnk.checkmate;

import java.io.Serializable;

/**
 * The way of storing student's answers.
 * Each instance contains student's answer itself and the grade for the answer.
 * @author vergeev
 */
public class TestAnswer implements Serializable {
    
    private final String answer;
    
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
        if (grade == null) {
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
}