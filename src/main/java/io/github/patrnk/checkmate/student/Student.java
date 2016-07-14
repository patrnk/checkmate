package io.github.patrnk.checkmate.student;


public final class Student {
    
    private final String name;
    
    public String getName() {
        return name;
    }
    
    private final String id;
    
    public String getId() {
        return id;
    }
    
    public Student(String name, String id) 
        throws BadStudentNameException, BadStudentIdException {
        
        checkName(name);
        checkId(id);
        this.name = name;
        this.id = id;
    }
    
    private void checkName(String name) throws BadStudentNameException {
        if (name.length() > 200) {
            throw new BadStudentNameException();
        }
    }
    
    private void checkId(String id) throws BadStudentIdException {
        if (id.length() > 200) {
            throw new BadStudentIdException();
        }
    }
}
