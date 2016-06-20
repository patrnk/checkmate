package io.github.patrnk.checkmate;

public enum CheckType {
    SIMPLE("SimpleTest"), STANDART("Standart"), ADVANCED("Advanced");
    
    String subclassName;
    
    CheckType(String name) {
        subclassName = name;
    }
    
    /**
     * Return the name of corresponding Test subclass.
     * @return the exact name of the subclass
     */
    @Override
    public String toString() {
        return subclassName;
    }
}
