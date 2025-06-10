import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private final LocalDateTime dateTime;
    private final String user;
    private final OperationType operationType;
    private final BigDecimal amount;
    private final String target;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Log(
            LocalDateTime dateTime,
            String user,
            OperationType operationType,
            BigDecimal amount,
            String target
    ) {
        this.dateTime = dateTime;
        this.user = user;
        this.operationType = operationType;
        this.amount = amount;
        this.target = target;
    }

    public enum OperationType {
        BALANCE_INQUIRY,
        TRANSFERRED,
        WITHDREW,
        RECEIVED
    }

    @Override
    public String toString() {
        String dateString = "[" + dateTime.format(FORMATTER) + "]";
        String formattedAmount;
        if (amount.scale() <= 0) {
            formattedAmount = amount.stripTrailingZeros().toPlainString();
        } else {
            formattedAmount = String.format("%.2f", amount.doubleValue()).replace(',', '.');
        }
        return switch (operationType) {
            case BALANCE_INQUIRY ->
                    String.format("%s %s balance inquiry %s", dateString, user, formattedAmount);
            case TRANSFERRED ->
                    String.format("%s %s transferred %s to %s", dateString, user, formattedAmount, target);
            case WITHDREW ->
                    String.format("%s %s withdrew %s", dateString, user, formattedAmount);
            case RECEIVED ->
                    String.format("%s %s received %s from %s", dateString, user, formattedAmount, target);
        };
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getUser() {
        return user;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTarget() {
        return target;
    }
}