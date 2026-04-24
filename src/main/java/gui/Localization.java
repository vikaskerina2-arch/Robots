package gui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Класс локализации
 * Подерживает русский и английский языки
 */
public class Localization {

    private static volatile Localization instance;
    private ResourceBundle bundle;
    private Locale currentLocale;

    private static final String BUNDLE_NAME = "messages";

    /**
     * Приватный конструктор (Singleton)
     */
    private Localization() {
        currentLocale = new Locale("ru");
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }

    /**
     * Единственный экземпляр
     */
    public static Localization getInstance() {
        if (instance == null) {
            synchronized (Localization.class) {
                if (instance == null) {
                    instance = new Localization();
                }
            }
        }
        return instance;
    }

    /**
     * Устанавливаем локаль
     */
    public void setLocaleFromString(String language) {
        if (currentLocale.getLanguage().equals(language)) return;
        this.currentLocale = new Locale(language);
        this.bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }

    /**
     * Строку по ключу
     */
    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * Строку с форматированием
     */
    public String getString(String key, Object... args) {
        String pattern = getString(key);
        if (args.length == 0) {
            return pattern;
        }
        return java.text.MessageFormat.format(pattern, args);
    }

    /**
     * Возвращаем текущую локаль
     */
    public String getLocaleString() {
        return currentLocale.getLanguage();
    }

}