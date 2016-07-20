package io.github.patrnk.checkmate.persistence;

import io.github.patrnk.checkmate.BadTestIdException;
import io.github.patrnk.checkmate.CmUtils;
import io.github.patrnk.checkmate.Test;
import io.github.patrnk.checkmate.TestAnswer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class PersistenceManager {
    
    private final static String SUFFIX_SEPARATOR = ".";
    
    private final static String TESTS_FOLDER = "tests";
    private final static String TESTS_SUFFIX = "tst";
       
    /**
     * Persists test over time serializing it into a file.
     * @param t test to serialize
     * @throws BadTestIdException if the test with that id already exists. 
     */
    public static void writeDownTest(Test t) throws BadTestIdException, IOException {
        new File(TESTS_FOLDER).mkdir();
        String filename = t.getInfo().getId().toString();
        filename += SUFFIX_SEPARATOR + TESTS_SUFFIX;
        String filePath = TESTS_FOLDER + File.separator + filename;
        if ((new File(filePath)).exists()) {
            throw new BadTestIdException("A test with id " + filename + 
                " already exists.");
        }
        writeDown(filePath, t);
    }
    
    private final static String ANSWER_FOLDER = "answers";
    private final static String ANSWER_SUFFIX = "ans";
    
    /**
     * Writes down the information provided.
     * @param studentName name of a student that owns the results
     * @param studentId id of a student that owns the results
     * @param answers student answers
     * @param testId the id of the test the answers belong to
     * @throws IOException if the method couldn't store the results
     * @throws BadStudentNameException, BadStudentIdException for the same 
     *      reason Record constructor throws them.
     */
    public static void writeDownTestResults(String studentName, String studentId, 
        List<TestAnswer> answers, Integer testId) 
        throws BadStudentNameException, BadStudentIdException, IOException {
        
        new File(ANSWER_FOLDER).mkdir();
        String filename = getUniqueRandomFilename(ANSWER_FOLDER, ANSWER_SUFFIX);
        String filepath = ANSWER_FOLDER + File.separator + filename;
        writeDown(filepath, answers);
        Record newRecord = new Record(testId, studentName, studentId, filename);
        try {
            Database.addRecord(newRecord);
        } catch (SQLException ex) {
            deleteFile(filepath);
            throw new IOException("Something went wrong with the DB.");
        }
    }
    
    /**
     * @return filename that's unique in the directory folderPath. 
     *      The filename contains suffix
     */
    private static String getUniqueRandomFilename(String folderPath, String suffix) {
        String filename = getRandomFileName() + SUFFIX_SEPARATOR + suffix;
        String filepath = folderPath + File.separator + filename;
        while (new File(filepath).exists()) {
            filename = getRandomFileName() + SUFFIX_SEPARATOR + suffix;
            filepath = folderPath + File.separator + filename;
        }
        return filename;
    }
   
    /**
     * @return a random number in a form of a string.
     */
    private static String getRandomFileName() {
        Double d = (Math.random() * 1000000);
        Integer i = d.intValue();
        return i.toString();
    }
    
    /**
     * Assumes that all the directories provided exist.
     * If it's not the case, throws IOException.
     */
    private static void writeDown(String filepath, Object o) 
        throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(o);
        oos.flush();
    }
    
    /**
     * Returns the tests stored in some long-term storage.
     * @return list of deserialized tests. 
     *      In case of a failure returns empty list.
     */
    public static List<Test> getExistingTests() {
        List<Test> tests = new ArrayList();
        List<Object> potentialTests = getExistingObjects(
            TESTS_FOLDER, TESTS_SUFFIX);
        for (Object potentialTest : potentialTests) {
            try { 
                Test t = (Test) potentialTest;
                tests.add(t);
            } catch (ClassCastException ex) {
                // TODO: log the thing but don't disturb the user, it's no big deal
            }
        }
        return tests;
    }
    
    /**
     * Gets you serialized objects in the directory.
     * Assumes that objects serialized into files with specified suffix
     * and are saved into the specified folder.
     * @param folderPath a folder where to look for files that contain 
     *      serialized objects
     * @param suffix suffix of the files that contain serialized objects. 
     *      Others are ignored
     * @return list of deserialized objects
     */
    private static List<Object> getExistingObjects(
            String folderPath, String suffix) {
        List<Object> objects = new ArrayList();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (getSuffix(file.getName()).equals(suffix)) {
                try {
                    FileInputStream in = new FileInputStream(file.getPath());
                    ObjectInputStream ois = new ObjectInputStream(in);
                    Object o = ois.readObject();
                    objects.add(o);
                } catch (IOException | ClassNotFoundException ex) {
                    // TODO: find a better way to handle the exception (in case of an attack)
                    // print a warning or something, but don't shut down.
                    CmUtils.printExceptionAndExit(ex);
                }
            }
        }
        return objects;
    }
    
    /**
     * Gets you all stored test results.
     * @return list of Records object with data.
     *      In case of failure, returns an empty list.
     */
    public static List<Record> getExistingTestResults() {
        List<Record> records = new ArrayList();
        try {
            records = Database.fetchRecords();
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return records;
    }
  
    /**
     * Return suffix of the filename.
     * Suffix is the part of the filename that comes after SUFFIX_SEPARATOR.
     * Never returns null.
     * @param filename a string that is treated as a name of a file.
     * @return suffix or empty string if the filename doesn't have one.
     */
    private static String getSuffix(String filename) {
        String suffix = "";
        Integer suffixIndex = filename.lastIndexOf(SUFFIX_SEPARATOR);
        if (suffixIndex != -1) {
            suffix = filename.substring(suffixIndex + 1);
        }
        return suffix;
    }
    
    /**
     * Removes the test from long-term storage.
     * @param test the test to remove
     * @throws IOException if not succeeded.
     */
    public static void deleteTest(Test test) throws IOException {
        String filepath = TESTS_FOLDER;
        filepath += File.separator;
        filepath += test.getInfo().getId().toString();
        filepath += SUFFIX_SEPARATOR;
        filepath += TESTS_SUFFIX;
        deleteFile(filepath);
    }
    
    private static void deleteFile(String filepath) throws IOException {
        if (!(new File(filepath)).delete()) {
            throw new IOException("File cannot be deleted: " + filepath);
        }
    }
    
    private PersistenceManager() {
        throw new AssertionError("You cannot instantiate PersistenceManager");
    }
}
