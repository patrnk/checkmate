package io.github.patrnk.checkmate.persistence;

import java.io.File;
import java.io.IOException;


class PathManager {
    
    private final String COMMON_FOLDER = "CheckMate_data";
    private final String TEST_FOLDER = "tests";
    private final String RESULT_FOLDER = "results";
    
    /**  
     * Returns the folder path where you can store Test objects.
     * The returned string has File.separator as last character for convenience.
     * Ensures that the directory actually exists.
     * @throws IOException if the directory doesn't exist and cannot be created.
     */
    public String getTestFolderPath() throws IOException {
        String folder = constructTestFolderPath();
        ensureFileExists(folder);
        return folder;
    }
    
    private String constructTestFolderPath() {
        return COMMON_FOLDER + File.separator + TEST_FOLDER + File.separator;
    }
    
    /**
     * Returns the folder path where you can store test results.
     * The returned string has File.separator as last character for convenience.
     * Ensures that the directory actually exists.
     * @throws IOException if the directory doesn't exist and cannot be created.
     */
    public String getResultFolderPath() throws IOException {
        String folder = constructResultFolderPath();
        ensureFileExists(folder);
        return folder;
    }
    
    private String constructResultFolderPath() {
        return COMMON_FOLDER + File.separator + RESULT_FOLDER + File.separator;
    }
    
    private void ensureFileExists(String directory) throws IOException {
        File folder = new File(directory);
        if (!folder.mkdir() && !folder.exists()) {
            throw new IOException("Can't create a file or folder here: " + 
                folder.getCanonicalPath());
        }
    }
    
    private final String SUFFIX_SEPARATOR = ".";
    private final String TEST_SUFFIX = "tst";
    private final String RESULT_SUFFIX = "res";
    
    /**
     * Formats the name of the file that's intended for Test object. 
     * @param name the name to format.
     * @return the formatted name.
     */
    public String getTestFileName(String name) {
        return name + SUFFIX_SEPARATOR + TEST_SUFFIX;
    }
    
    /**
     * Formats the name of the file that's intended for test results. 
     * @param name the name to format.
     * @return the formatted name.
     */
    public String getResultFileName(String name) {
        return name + SUFFIX_SEPARATOR + RESULT_SUFFIX;
    }
}
