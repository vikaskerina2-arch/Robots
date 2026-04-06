package log;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Источник сообщений лога, хранение записей
 */
public class LogWindowSource {
    private int queueLength;

    // Слушатели
    private final WeakArrayList<LogChangeListener> listeners;

    /**
     * Кэш слушателей для доступа, обновляется
     */
    private volatile LogChangeListener[] activeListeners;

    // Для записей
    private final Queue<LogEntry> messages;

    /**
     * Новый источник лога
     */
    public LogWindowSource(int iQueueLength) {
        this.queueLength = iQueueLength;
        this.messages = new ConcurrentLinkedQueue<>();
        this.listeners = new WeakArrayList<>();
    }

    /**
     * Регистрация слушателя
     */
    public void registerListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            activeListeners = null;  // сбросить кэш
        }
    }

    /**
     * Удаление слушателя
     */
    public void unregisterListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
            activeListeners = null;  // сбросить кэш
        }
    }

    /**
     * Добавление сообщения в лог и уведомление
     */
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        messages.add(entry);

        while (messages.size() > queueLength) {
            // -начало
            messages.poll();
        }

        // Уведомление
        LogChangeListener [] activeListeners = this.activeListeners;
        if (activeListeners == null) {
            synchronized (listeners) {
                if (this.activeListeners == null) {
                    activeListeners = listeners.toLiveArray(new LogChangeListener [0]);
                    this.activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners) {
            if (listener != null) {
                listener.onLogChanged();
            }
        }
    }

    /**
     * Все записи
     */
    public List<LogEntry> all() {
        return new ArrayList<>(messages);
    }

    /**
     * Итератор не ломается если добавлять в процессе
     */
    public Iterable<LogEntry> safeIterable() {
        return () -> all().iterator();
    }

    /**
     * Возвращает количество записей в логе
     */
    public int size() {
        return messages.size();
    }

    /**
     * Доступ по индексам
     */
    public List<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= messages.size()) {
            return Collections.emptyList();
        }
        int realCount = Math.min(count, messages.size() - startFrom);
        List<LogEntry> result = new ArrayList<>(realCount);

        int index = 0;
        for (LogEntry entry : messages) {
            if (index >= startFrom && index < startFrom + realCount) {
                result.add(entry);
            }
            index++;
            if (index >= startFrom + realCount) {
                break;
            }
        }
        return result;
    }
}