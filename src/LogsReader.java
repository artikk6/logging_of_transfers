import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LogsReader {
    public static List<String> readLogs() {
        List<String> logs = new ArrayList<>();
        Path path = Paths.get("logs");
        if (!Files.isDirectory(path)) {
            throw new IllegalStateException("Directory not found");
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.log")) {
            for (Path file : directoryStream) {
                logs.addAll(Files.readAllLines(file));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (logs.isEmpty()) {
            throw new IllegalStateException("Log folder is empty");
        }
        return logs;
    }
}