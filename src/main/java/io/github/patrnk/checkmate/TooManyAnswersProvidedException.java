package io.github.patrnk.checkmate;

//TODO: document TooManyAnswersProvidedException
public class TooManyAnswersProvidedException extends Exception {
    public TooManyAnswersProvidedException() {
        super();
    }
    public TooManyAnswersProvidedException(String message) {
        super(message);
    }
}