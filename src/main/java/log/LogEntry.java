package log;

/**
 * Одна запись в логе
 */
public class LogEntry
{
    private LogLevel logLevel;
    private String strMessage;

    /**
     * Создание новой записи лога
     * @param logLevel
     * @param strMessage
     */
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        this.strMessage = strMessage;
        this.logLevel = logLevel;
    }

    /**
     * Возврат текста сообщения в логе
     */
    public String getMessage()
    {
        return strMessage;
    }

    /**
     * Возврат уровня важности сообщения
     */
    public LogLevel getLevel()
    {
        return logLevel;
    }
}

