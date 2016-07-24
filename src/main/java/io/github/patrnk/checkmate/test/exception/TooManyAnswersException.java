package io.github.patrnk.checkmate.test.exception;


public class TooManyAnswersException extends BadTestInfoException {
    
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
