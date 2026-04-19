package log;

import java.util.*;

/**
 * Источник сообщений лога, хранение записей
 */
public class LogWindowSource {
    private int maxSize;

    // Слушатели
    private final WeakArrayList<LogChangeListener> listeners;

    /**
     * Кэш слушателей для доступа, обновляется
     */
    private volatile LogChangeListener[] activeListeners;

    // Для записей
    private final Deque<LogEntry> messages;

    /**
     * Новый источник лога
     */
    public LogWindowSource(int maxSize) {
        this.maxSize = maxSize;
        this.messages = new ArrayDeque<>(maxSize + 1);
        this.listeners = new WeakArrayList<>();
    }

    /**
     * Регистрация слушателя
     */
    public void registerListener(LogChangeListener listener) {
        if (listener == null) return;
        synchronized (listeners) {
            for (LogChangeListener existing : getLiveListeners()) {
                if (existing == listener) return;
            }
            listeners.add(listener);
            activeListeners = null;
        }
    }

    /**
     * Удаление слушателя
     */
    public void unregisterListener(LogChangeListener listener) {
        if (listener == null) return;
        synchronized (listeners) {
            listeners.remove(listener);
            activeListeners = null;
        }
    }

    /**
     * Добавление сообщения в лог
     */
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        synchronized (this) {
            if (messages.size() >= maxSize) {
                messages.pollFirst();
            }
            messages.offerLast(entry);
        }
        notifyListeners();
    }


    // Уведомление
    private void notifyListeners() {
        LogChangeListener[] active = activeListeners;
        if (active == null) {
            synchronized (listeners) {
                if (activeListeners == null) {
                    List<LogChangeListener> live = getLiveListeners();
                    activeListeners = live.toArray(new LogChangeListener[0]);
                    active = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : active) {
            if (listener != null) {
                try {
                    listener.onLogChanged();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Список живых слушателей
     */
    private List<LogChangeListener> getLiveListeners() {
        synchronized (listeners) {
            return listeners.getLiveElements();
        }
    }

    /**
     * Все записи
     */
    public List<LogEntry> all() {
        synchronized (this) {
            return new ArrayList<>(messages);
        }
    }

    /**
     * Итератор не ломается если добавлять в процессе
     */
    public Iterable<LogEntry> safeIterable () {
        return () -> all().iterator();
    }

    /**
     * Возвращает количество записей в логе
     */
    public int size () {
        synchronized (this) {
            return messages.size();
        }
    }

    /**
     * Доступ по индексам
     */
    public List<LogEntry> range ( int startFrom, int count){
        List<LogEntry> snapshot = new ArrayList<>();
        for (LogEntry entry : safeIterable()) {
            snapshot.add(entry);
        }

        if (startFrom < 0 || startFrom >= snapshot.size()) {
            return Collections.emptyList();
        }
        int resultCount = Math.min(count, snapshot.size() - startFrom);
        return new ArrayList<>(snapshot.subList(startFrom, startFrom + resultCount));
    }
}