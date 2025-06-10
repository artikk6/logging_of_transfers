import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> rawLogs = LogsReader.readLogs();
        List<Log> logs = ParsingLogs.parseLogs(rawLogs);
        LogsWriter.writeByUser(logs);
    }
}
