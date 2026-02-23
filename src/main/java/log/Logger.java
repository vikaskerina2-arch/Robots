package log;

/**
 * Интерфейс записи сообщений в лог
 */
public final class Logger
{
    /**
     * 1 экземпляр лога для всех приложений
     */
    private static final LogWindowSource DEFAULT_LOG_SOURCE =
            new LogWindowSource(100);

    /**
     * Конструктор для обхода создания экземпляров
     */
    private Logger()
    {
    }

    /**
     * Отладочное сообщение в лог
     * @param strMessage
     */
    public static void debug(String strMessage)
    {
        DEFAULT_LOG_SOURCE.append(LogLevel.Debug, strMessage);
    }

    /**
     * Сообщение об ошибке в лог
     * @param strMessage
     */
    public static void error(String strMessage)
    {
        DEFAULT_LOG_SOURCE.append(LogLevel.Error, strMessage);
    }

    /**
     * Ссылка на 1 экземпляр на лог
     * @return
     */
    public static LogWindowSource getDefaultLogSource()
    {
        return DEFAULT_LOG_SOURCE;
    }
}
