package io.github.patrnk.checkmate.student;


public class BadStudentNameException extends Exception {
    
    public BadStudentNameException() {
        super();
    }
    
    public BadStudentNameException(String message) {
        super(message);
    }
    
    public BadStudentNameException(Throwable cause) {
        super(cause);
    }

    public BadStudentNameException(String message, Throwable cause) {
        super(message, cause);
    }  
}
