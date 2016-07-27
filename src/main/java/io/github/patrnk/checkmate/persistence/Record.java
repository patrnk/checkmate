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
     
    private final String answerFileName;
    
    public String getAnswerFileName() {
        return answerFileName;
    }
    
    public Record(Integer testId, String studentName, 
            String studentId, String answerFileName) 
            throws BadStudentNameException, BadStudentIdException {   
        checkName(studentName);
        checkId(studentId);
        this.testId = testId;
        this.studentName = studentName;
        this.studentId = studentId;
        this.answerFileName = answerFileName;
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
}
