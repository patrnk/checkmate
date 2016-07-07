package io.github.patrnk.checkmate;


public class AnswerNotProvidedException extends BadTestInfoException {
    
    private final Integer questionNumber;
    
    public Integer getQuestionNumber() {
        return questionNumber;
    }
    
    public AnswerNotProvidedException(int questionNumber) {
        super();
        if (questionNumber <= 0) {
            throw new IllegalArgumentException("Number of the question must be "
                + "positive and not " + questionNumber);
        }
        this.questionNumber = questionNumber;
    }
    
    public AnswerNotProvidedException(String message, int questionNumber) {
        super(message);
        if (questionNumber <= 0) {
            throw new IllegalArgumentException("Number of the question must be "
                + "positive and not " + questionNumber);
        }
        this.questionNumber = questionNumber;
    }
    
    public AnswerNotProvidedException(Throwable cause, int questionNumber) {
        super(cause);
        if (questionNumber <= 0) {
            throw new IllegalArgumentException("Number of the question must be "
                + "positive and not " + questionNumber);
        }
        this.questionNumber = questionNumber;
    }
    
    public AnswerNotProvidedException(String message, Throwable cause, 
        int questionNumber) {
        super(message, cause);
        if (questionNumber <= 0) {
            throw new IllegalArgumentException("Number of the question must be "
                + "positive and not " + questionNumber);
        }
        this.questionNumber = questionNumber;
    }
}
