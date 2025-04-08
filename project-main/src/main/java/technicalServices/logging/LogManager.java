/**
 * Manages logging operations for the application, including writing informational and error logs to separate files.
 * Provides utility methods for logging different levels of messages with timestamps.
 * Logs are stored in text files located under the `technicalServices/logging` package.
 */

package technicalServices.logging;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class LogManager {
    // this class logs all the actions taken in the app to the appropriate txt files, these files are under this package
    // Formatter instance is a final because we want same date format in every log operation
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");
    /**
     *
     * @param message is the actions that we want to describe
     */
    public static void logInfo(String message) {
        logbasedonFile("src/main/java/technicalServices/logging/application_info.txt", "[INFO] " + message);
    }


    /**
     *
     * @param message is the actions that we want to describe
     */    public static void logError(String message) {
        logbasedonFile("src/main/java/technicalServices/logging/application_error.txt", "[ERROR] " + message);
    }
    /**
     *
     * @param filename is the name of the file that the information will be written
     * @param logMessage messages we want to write to the file
     */
    private static void logbasedonFile(String filename, String logMessage) {
        // if there is an error in opening file, it will be catched
        try (BufferedWriter openedFile = new BufferedWriter(new FileWriter(filename, true))) {
            // taking the current date and time
            LocalDateTime currentDate = LocalDateTime.now();
            // formating current date with the format i determined earlier
            String formattedDateTime = currentDate.format(FORMATTER);
            //writing to the file
            openedFile.write("[" + formattedDateTime + "]" + logMessage+"\n");
            // closing the file because if we don't close, the written information will not be seen
            openedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}