package io.github.patrnk.checkmate.test.exception;


public class TooManyQuestionsException extends BadTestInfoException {
    
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
