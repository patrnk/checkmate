package io.github.patrnk.checkmate;

// TODO: document IncorrectQuestionNumberException
public class IncorrectQuestionNumberException extends Exception {
    public IncorrectQuestionNumberException() {
        super();
    }
    public IncorrectQuestionNumberException(String message) {
        super(message);
    }
    public IncorrectQuestionNumberException(String message, int errorOffset) {
        super(message);
        this.errorOffset = errorOffset;
    }
    
    private int errorOffset = -1;
    public int getErrorOffset() {
        return errorOffset;
    }
}
