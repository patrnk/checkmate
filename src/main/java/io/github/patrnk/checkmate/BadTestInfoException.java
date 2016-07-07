package io.github.patrnk.checkmate;


public class BadTestInfoException extends Exception {
    
    public BadTestInfoException() {
        super();
    }
    
    public BadTestInfoException(String message) {
        super(message);
    }
    
    public BadTestInfoException(Throwable cause) {
        super(cause);
    }
    
    public BadTestInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
