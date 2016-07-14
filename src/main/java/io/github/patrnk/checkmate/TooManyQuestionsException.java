package io.github.patrnk.checkmate;


public class TooManyQuestionsException extends Exception {
    
    public TooManyQuestionsException() {
        super();
    }
    
    public TooManyQuestionsException(String message) {
        super(message);
    }
    
    public TooManyQuestionsException(Throwable cause) {
        super(cause);
    }

    public TooManyQuestionsException(String message, Throwable cause) {
        super(message, cause);
    }  
}
