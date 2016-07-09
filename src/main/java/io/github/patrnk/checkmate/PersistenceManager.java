package io.github.patrnk.checkmate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public final class PersistenceManager {
    
    private final String TESTS_FOLDER = "tests";
    
    /**
     * Persists test over time serializing it into a file.
     * @param t test to serialize
     * @throws BadTestIdException if the test with that id already exists. 
     */
    public void writeDownTest(Test t) throws BadTestIdException {
        String filename = t.getInfo().getId().toString();
        if (alreadyExists(TESTS_FOLDER, filename)) {
            throw new BadTestIdException("A test with id " + filename + 
                " already exists.");
        }
        writeDown(TESTS_FOLDER, filename, t);
    }
    
    private Boolean alreadyExists(String folderPath, String name) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName() != null) {
                if (file.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void writeDown(String folderPath, String filename, Object o) {
        try {
            String filepath = folderPath + File.separator + filename;
            FileOutputStream out = new FileOutputStream(filepath);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(o);
            oos.flush();
        } catch (Exception e) {
            CmUtils.printException(e);
        }
    }
}
