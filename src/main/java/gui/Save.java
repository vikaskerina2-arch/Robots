package gui;

import java.util.Map;

/**
 * Интерфейс для окон, которые умеют сохранять и восстанавливать своё состояние
 */
public interface Save {
    /**
     * Сохраняет состояние окна в словарь
     */
    Map<String, String> saveState();

    /**
     * Восстанавливает состояние окна из словаря
     * @param state
     */
    void restoreState(Map<String, String> state);

    /**
     * Возвращает префикс для ключей окна
     */
    String getPrefix();

    /**
     * Обновляет тексты при смене языка
     */
    void updateTextsLang();
}
