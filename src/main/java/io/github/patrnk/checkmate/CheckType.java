package io.github.patrnk.checkmate;

public enum CheckType {
    // TODO: substitute with real values 
    SIMPLE("SimpleTest"), STANDART("Standart"), ADVANCED("Advanced");
    
    private final String subclassName;
    
    /**
     * @param name should be a class name (like "java.util.ArrayList")
     *      of a subclass. It will be used for Class.forName()
     */
    CheckType(String name) {
        subclassName = name;
    }
    
    /**
     * Return the name of corresponding Test subclass.
     * @return the name of the subclass which can be used for Class.forName()
     *      (although there's no guarantee that the call will succeed).
     */
    @Override
    public String toString() {
        return subclassName;
    }
    
    /**
     * @return returns corresponding CheckType.
     */
    public CheckType fromString(String subclassName) {
        for (CheckType c : CheckType.values()) {
            if (c.subclassName.equals(subclassName)) {
                return c;
            }
        }
        throw new IllegalArgumentException("No constant with subclass name "
            + subclassName + " found.");
    }
}
