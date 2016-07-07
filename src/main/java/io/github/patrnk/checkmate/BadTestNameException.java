package io.github.patrnk.checkmate;


public class BadTestNameException extends BadTestInfoException {
    
    public BadTestNameException() {
        super();
    }
    
    public BadTestNameException(String message) {
        super(message);
    }
    
    public BadTestNameException(Throwable cause) {
        super(cause);
    }
    
    public BadTestNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
