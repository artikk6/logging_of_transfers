import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LogsWriter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void writeByUser(List<Log> logs) {
        List<Log> users = new ArrayList<>(logs);
        for (Log log : logs) {
            if (log.getOperationType() == Log.OperationType.TRANSFERRED) {
                users.add(new Log(
                        log.getDateTime(),
                        log.getTarget(),
                        Log.OperationType.RECEIVED,
                        log.getAmount(),
                        log.getUser()
                ));
            }
        }
        Map<String, List<Log>> logsByUser = new HashMap<>();
        for (Log log : users) {
            logsByUser.computeIfAbsent(log.getUser(), user -> new ArrayList<>()).add(log);
        }
        Path transactions = Path.of("transactions_by_users");
        try {
            if (!Files.exists(transactions)) Files.createDirectory(transactions);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        for (Map.Entry<String, List<Log>> entry : logsByUser.entrySet()) {
            String user = entry.getKey();
            List<Log> userLogs = entry.getValue();

            userLogs.sort(Comparator.comparing(Log::getDateTime));

            BigDecimal balance = BigDecimal.ZERO;

            for (Log log : userLogs) {
                balance = switch (log.getOperationType()) {
                    case TRANSFERRED, WITHDREW -> balance.subtract(log.getAmount());
                    case BALANCE_INQUIRY -> log.getAmount();
                    case RECEIVED -> balance.add(log.getAmount());
                };
            }

            Path userFile = transactions.resolve(user + ".log");
            try {
                List<String> lines = new ArrayList<>();
                for (Log log : userLogs) {
                    lines.add(log.toString());
                }
                String lastDate = userLogs.get(userLogs.size() - 1).getDateTime().format(FORMATTER);
                lines.add(
                        String.format("[%s] %s final balance %s",
                                lastDate,
                                user,
                                balance.stripTrailingZeros().toPlainString()));
                Files.write(userFile, lines);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}