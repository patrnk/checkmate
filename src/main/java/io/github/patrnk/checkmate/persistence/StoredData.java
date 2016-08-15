package io.github.patrnk.checkmate.persistence;

import java.io.File;
import java.io.IOException;


final class StoredData {

    private final static String COMMON_FOLDER = "CheckMate_data";
    
    private final static String SUFFIX_SEPARATOR = ".";
    
    private final String folderName;
    
    private final String suffix;

    public StoredData(String folderName, String fileSuffix) {
        this.folderName = folderName;
        this.suffix = fileSuffix;
    }
    
    /**  
     * Returns the folder path where you can store data.
     * Ensures that the directory actually exists.
     * @throws IOException if the directory doesn't exist and cannot be created.
     */
    public String getFolderPath() throws IOException {
        String folder = constructFolderPath();
        ensureFileExists(folder);
        return folder;
    }
    
    private String constructFolderPath() {
        return COMMON_FOLDER + File.separator + folderName;
    }
    
    private void ensureFileExists(String directory) throws IOException {
        File folder = new File(directory);
        if (!folder.mkdir() && !folder.exists()) {
            throw new IOException("Can't create a file or folder here: " + 
                folder.getCanonicalPath());
        }
    }
    
    /**
     * Returns path which can be used to create the file.
     * Works in the same way as getFilePath(String filename) 
     *      but generates the name itself.
     * Cannot be used to create more than a billion of files.
     * @return path which can be used to create the file.
     * @throws IOException if the folder of the file doesn't exist 
     *      and cannot be created.
     */    
    public String getFilePath() throws IOException {
        String randomName = this.getRandomNumberString();
        String filepath = this.getFilePath(randomName);
        while (new File(filepath).exists()) {
            randomName = this.getRandomNumberString();
            filepath = this.getFilePath(randomName);
        }
        return filepath;
    }
    
    /**
     * Returns path which can be used to create the result file.
     * @return path which can be used to create the result file.
     * @throws IOException if the folder of the file doesn't exist 
     *      and cannot be created.
     */
    public String getFilePath(String filename) throws IOException {
        String formattedName = getResultErrorFileName(filename);
        return this.getFolderPath() + File.separator + formattedName;
    }
    
    private String getResultErrorFileName(String name) {
        return name + SUFFIX_SEPARATOR + suffix;
    }
   
    /**
     * Returns a random number [0; 10000000] in a form of a string.
     */
    private String getRandomNumberString() {
        Double d = (Math.random() * 10000000);
        Integer i = d.intValue();
        return i.toString();
    }
}
