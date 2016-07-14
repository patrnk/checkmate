package io.github.patrnk.checkmate;


public class TooManyAnswersException extends Exception {
    
    public TooManyAnswersException() {
        super();
    }
    
    public TooManyAnswersException(String message) {
        super(message);
    }
    
    public TooManyAnswersException(Throwable cause) {
        super(cause);
    }

    public TooManyAnswersException(String message, Throwable cause) {
        super(message, cause);
    }
}
