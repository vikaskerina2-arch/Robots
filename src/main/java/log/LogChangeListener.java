package log;

/**
 * Получение уведомлений об изменениях в логе
 */
public interface LogChangeListener
{
    /**
     * Вызов при изменении лога
     */
    void onLogChanged();
}
