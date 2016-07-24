package io.github.patrnk.checkmate.test.exception;


public class BadTestIdException extends BadTestInfoException {
    
    public BadTestIdException() {
        super();
    }
    
    public BadTestIdException(String message) {
        super(message);
    }
    
    public BadTestIdException(Throwable cause) {
        super(cause);
    }

    public BadTestIdException(String message, Throwable cause) {
        super(message, cause);
    }    
}
