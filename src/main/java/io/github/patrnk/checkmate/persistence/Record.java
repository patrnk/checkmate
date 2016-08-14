package io.github.patrnk.checkmate.persistence;


public final class Record {
    
    private final Integer testId;
    
    public Integer getTestId() {
        return testId;
    }
    
    private final String studentName;
    
    public String getStudentName() {
        return studentName;
    }
    
    private final String studentId;
    
    public String getStudentId() {
        return studentId;
    }
     
    private final String resultFilename;
    
    public String getResultFilename() {
        return resultFilename;
    }
    
    public Record(Integer testId, String studentName, 
            String studentId, String resultFilename) 
            throws BadStudentNameException, BadStudentIdException {   
        checkName(studentName);
        checkId(studentId);
        checkResultFilename(resultFilename);
        this.testId = testId;
        this.studentName = studentName;
        this.studentId = studentId;
        this.resultFilename = resultFilename;
    }

    private void checkName(String name) throws BadStudentNameException {
        if (name == null || name.length() > 200) {
            throw new BadStudentNameException("The name is too long or null.");
        }
    }
    
    private void checkId(String id) throws BadStudentIdException {
        if (id == null || id.length() > 200) {
            throw new BadStudentIdException("The id is too long or null.");
        }
    }
    
    private void checkResultFilename(String name) {
        if (name == null || name.length() > 250) {
            throw new IllegalArgumentException("resultFilename cannot be more "
                + "than 250 characters long.");
        }
    }
}
