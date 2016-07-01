package io.github.patrnk.checkmate;

import java.util.ArrayList;
import java.util.List;


public final class StudentAnswers {
    private final List<String> answers;
    
    public StudentAnswers(Integer numberOfQuestions) {
        answers = new ArrayList();
        while(answers.size() < numberOfQuestions) answers.add(null);
    }
   
    /**
     * 
     * @param number from 1 to lastQuestionNumber
     * @param answer 
     */
    public void putAnswer(int number, String answer) {
        int index = number - 1;
        this.checkBounds(index);
        answers.set(index, answer);
    }

    public String getAnswer(int number) {
        int index = number - 1;
        this.checkBounds(index);
        return answers.get(index);
    }
    
    /**
     * Returns the number of the last question.
     * Allows iteration using plain old for loop.
     */
    public Integer lastQuestionNumber() {
        return answers.size();
    }
    
    private void checkBounds(Integer questionNumber) {
        if (questionNumber < 0) {
            throw new IllegalArgumentException("questionNumber must be positive "
                + "integer, not " + questionNumber);
        }
        if (questionNumber >= this.lastQuestionNumber()) {
            throw new IllegalArgumentException("questionNumber " + "("
                + questionNumber + ")" + "cannot be greater than "
                + "the last question's number (" + this.lastQuestionNumber() 
                + ") ");
        }
    }
    
}
