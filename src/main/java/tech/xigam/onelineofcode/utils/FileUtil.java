package tech.xigam.onelineofcode.utils;

import tech.xigam.onelineofcode.OneLineOfCode;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public final class FileUtil {
    public static String readFile(File file) {
        var builder = new StringBuilder(); try {
            var scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            } scanner.close();
        } catch (Exception e) {
            OneLineOfCode.logger.error("Error reading file: " + file.getAbsolutePath(), e);
        } return builder.toString();
    }
    
    public static void writeToFile(File file, String content) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print(content); writer.close();
        } catch (Exception e) {
            OneLineOfCode.logger.error("Error writing to file: " + file.getAbsolutePath(), e);
        }
    }
    
    public static boolean checkFiles() {
        var directory = new File(System.getProperty("user.dir"));
        
        var activity = new File(directory, "activity.json");
        return activity.exists();
    }
}
