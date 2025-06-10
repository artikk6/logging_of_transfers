import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingLogs {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Pattern BALANCE_PATTERN = Pattern
            .compile("^\\[(.+)] (\\w+) balance inquiry ([\\d.]+)$");

    private static final Pattern TRANSFER_PATTERN = Pattern
            .compile("^\\[(.+)] (\\w+) transferred ([\\d.]+) to (\\w+)$");

    private static final Pattern WITHDRAW_PATTERN = Pattern
            .compile("^\\[(.+)] (\\w+) withdrew ([\\d.]+)$");

    public static List<Log> parseLogs(List<String> logs) {
        List<Log> entries = new ArrayList<>();
        for (String log : logs) {
            Log parsed = parseLog(log);
            if (parsed != null) {
                entries.add(parsed);
            }
        }
        return entries;
    }

    private static Log parseLog(String log) {
        Matcher matcher = BALANCE_PATTERN.matcher(log);

        if (matcher.matches()) {
            return new Log(
                    LocalDateTime.parse(matcher.group(1), FORMATTER),
                    matcher.group(2),
                    Log.OperationType.BALANCE_INQUIRY,
                    new BigDecimal(matcher.group(3)),
                    null
            );
        }

        matcher = TRANSFER_PATTERN.matcher(log);

        if (matcher.matches()) {
            return new Log(
                    LocalDateTime.parse(matcher.group(1), FORMATTER),
                    matcher.group(2),
                    Log.OperationType.TRANSFERRED,
                    new BigDecimal(matcher.group(3)),
                    matcher.group(4)
            );
        }

        matcher = WITHDRAW_PATTERN.matcher(log);
        if (matcher.matches()) {
            return new Log(
                    LocalDateTime.parse(matcher.group(1), FORMATTER),
                    matcher.group(2),
                    Log.OperationType.WITHDREW,
                    new BigDecimal(matcher.group(3)),
                    null
            );
        }

        return null;
    }
}