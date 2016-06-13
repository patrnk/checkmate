package io.github.patrnk.checkmate;


public class CmUtils {
    public static void printException(Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }
}
