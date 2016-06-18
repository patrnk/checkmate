package io.github.patrnk.checkmate;

public enum CheckType {
    SIMPLE, STANDART, ADVANCED;
    
    /**
     * Gets a name of corresponding GeneralTest subclass.
     * @return the exact name of the class
     * @throws UnsupportedOperationException if no name is provided 
     */
    @Override
    public String toString() throws UnsupportedOperationException {
        switch (this) {
//            case SIMPLE:
//                return "Simple";
//            case STANDART:
//                return "Standart";
//            case ADVANCED:
//                return "Advanced";
            default: 
                throw new UnsupportedOperationException("Add name of ");
        }
    } 
}
