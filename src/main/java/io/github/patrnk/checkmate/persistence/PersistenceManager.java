package io.github.patrnk.checkmate.persistence;

import io.github.patrnk.checkmate.BadTestIdException;
import io.github.patrnk.checkmate.CmUtils;
import io.github.patrnk.checkmate.Test;
import io.github.patrnk.checkmate.TestAnswer;
import io.github.patrnk.checkmate.student.Student;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public final class PersistenceManager {
    
    private final static String SUFFIX_SEPARATOR = ".";
    
    private final static String TESTS_FOLDER = "tests";
    private final static String TESTS_SUFFIX = "tst";
        
    /**
     * Persists test over time serializing it into a file.
     * @param t test to serialize
     * @throws BadTestIdException if the test with that id already exists. 
     */
    public static void writeDownTest(Test t) throws BadTestIdException {
        String filename = t.getInfo().getId().toString();
        filename += SUFFIX_SEPARATOR + TESTS_SUFFIX;
        String filePath = TESTS_FOLDER + File.separator + filename;
        if ((new File(filePath)).exists()) {
            throw new BadTestIdException("A test with id " + filename + 
                " already exists.");
        }
        writeDown(filePath, t);
    }
    
    public static void writeDownTestResults(Student student, 
        List<TestAnswer> answers, Integer testId) {
        // TODO: do the db thing.
    }
    
    private static void writeDown(String filePath, Object o) {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(o);
            oos.flush();
        } catch (Exception e) {
            CmUtils.printExceptionAndExit(e);
        }
    }
    
    /**
     * Returns the tests stored in some long-term storage.
     * @return list of deserialized tests
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
    
    private PersistenceManager() {
        throw new AssertionError("You cannot instantiate PersistenceManager");
    }
}
