package io.github.patrnk.checkmate;


public class MalformedTestDescriptionException extends BadTestInfoException {
    
    private final Integer badLine;
    
    public Integer getBadLine() {
        return badLine;
    }
    
    public MalformedTestDescriptionException(int badLine) {
        super();
        if (badLine <= 0) {
            throw new IllegalArgumentException("Number of the bad line must be "
                + "positive and not " + badLine);
        }
        this.badLine = badLine;
    }

    public MalformedTestDescriptionException(String message, int badLine) {
        super(message);
        if (badLine <= 0) {
            throw new IllegalArgumentException("Number of the bad line must be "
                + "positive and not " + badLine);
        }
        this.badLine = badLine;
    }

    public MalformedTestDescriptionException(Throwable cause, int badLine) {
        super(cause);
        if (badLine <= 0) {
            throw new IllegalArgumentException("Number of the bad line must be "
                + "positive and not " + badLine);
        }
        this.badLine = badLine;
    }
    
    public MalformedTestDescriptionException(String message, 
            Throwable cause, int badLine) {
        super(message, cause);    
        if (badLine <= 0) {
            throw new IllegalArgumentException("Number of the bad line must be "
                + "positive and not " + badLine);
        }
        this.badLine = badLine;
    }
}
