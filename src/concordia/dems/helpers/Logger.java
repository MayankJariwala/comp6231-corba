package concordia.dems.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public class Logger {

    private Logger() {

    }

    private static void init() {
        try {
            if (!Files.exists(Paths.get("./logs"))) {
                Files.createDirectories(Paths.get("./logs/clients"));
                Files.createDirectories(Paths.get("./logs/servers"));
                System.err.println("Directory Created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //[message format] - {server/client,"ID","request","response","Timestamp"}
    public static void writeLogToFile(String serverOrClient, String customerID, String request, String response, String timeStamp) {
        init();
        String filePath = serverOrClient.equalsIgnoreCase("server") ? "logs/servers/" : "logs/clients/";
        filePath += customerID.toLowerCase() + ".txt";
        String message = request.trim() + " | " + response.trim() + " | " + timeStamp.trim();
        File file = new File(filePath);
        file.setWritable(true);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.append(message).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
