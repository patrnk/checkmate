package io.github.patrnk.checkmate;


public class BadTestIdException extends CheckMateException {
    public BadTestIdException() {
        super();
    }
    
    public BadTestIdException(String message) {
        super(message);
    }
}
