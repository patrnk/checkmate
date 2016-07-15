package io.github.patrnk.checkmate.persistence;


public class BadStudentIdException extends Exception {

    public BadStudentIdException() {
        super();
    }
    
    public BadStudentIdException(String message) {
        super(message);
    }
    
    public BadStudentIdException(Throwable cause) {
        super(cause);
    }

    public BadStudentIdException(String message, Throwable cause) {
        super(message, cause);
    }  
}
