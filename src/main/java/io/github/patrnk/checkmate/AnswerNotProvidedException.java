package io.github.patrnk.checkmate;


public class AnswerNotProvidedException extends Exception {
    public AnswerNotProvidedException() {
        super();
    }
    public AnswerNotProvidedException(String message) {
        super(message);
    }
}
