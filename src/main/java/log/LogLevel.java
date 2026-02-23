package log;

/**
 * Уровни важности сообщений в логе
 */
public enum LogLevel
{
    Trace(0),
    Debug(1),
    Info(2),
    Warning(3),
    Error(4),
    Fatal(5);
    
    private final int level;

    /**
     * Конструктор перечисления
     * @param iLevel
     */
    LogLevel(int iLevel)
    {
        level = iLevel;
    }

    /**
     * Число уровня важности
     * @return
     */
    public int level()
    {
        return level;
    }
}

