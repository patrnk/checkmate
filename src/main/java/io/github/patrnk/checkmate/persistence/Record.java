package io.github.patrnk.checkmate.persistence;

/**
 * Entity that contains information about stored test results.
 */
public final class Record {
    
    private final Integer testId;
    
    /**
     * Returns id of the test to which the results belong to.
     */
    public Integer getTestId() {
        return testId;
    }
    
    private final String studentName;
    
    /**
     * Returns name of the student the result belongs to. 
     */
    public String getStudentName() {
        return studentName;
    }
    
    private final String studentId;
    
    /**
     * Returns id of the student the result belongs to.
     */
    public String getStudentId() {
        return studentId;
    }
     
    private final String resultFilepath;
    
    /**
     * Returns path to the file where the result is stored.
     */
    public String getResultFilepath() {
        return resultFilepath;
    }
    
    public Record(Integer testId, String studentName, 
            String studentId, String resultFilepath) 
            throws BadStudentNameException, BadStudentIdException {   
        checkName(studentName);
        checkId(studentId);
        checkResultFilename(resultFilepath);
        this.testId = testId;
        this.studentName = studentName;
        this.studentId = studentId;
        this.resultFilepath = resultFilepath;
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
