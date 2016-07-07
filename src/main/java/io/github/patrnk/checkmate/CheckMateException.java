package io.github.patrnk.checkmate;


public class CheckMateException extends Exception {
    public CheckMateException() {
        super();
    }
    public CheckMateException(String message) {
        super(message);
    }
    public CheckMateException(Throwable cause) {
        super(cause);
    }
    public CheckMateException(String message, Throwable cause) {
        super(message, cause);
    }
}
