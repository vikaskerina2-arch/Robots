package gui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Класс локализации приложения (Singleton)
 * Поддерживает русский и английский языки
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
        this("ru");
    }

    /**
     * Конструктор с указанием языка
     */
    private Localization(String langCode) {
        currentLocale = "en".equals(langCode) ? Locale.ENGLISH : new Locale("ru");
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }

    /**
     * Возвращает единственный экземпляр
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
     * Возвращает локализованную строку по ключу
     */
    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "???" + key + "???";
        }
    }

    /**
     * Возвращает локализованную строку с форматированием
     */
    public String get(String key, Object... args) {
        String pattern = get(key);
        if (args.length == 0) {
            return pattern;
        }
        return MessageFormat.format(pattern, args);
    }

    /**
     * Устанавливает новый язык
     */
    public void setLocale(String langCode) {
        Locale newLocale = "en".equals(langCode) ? Locale.ENGLISH : new Locale("ru");
        if (currentLocale.equals(newLocale)) {
            return;
        }
        currentLocale = newLocale;
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }

    /**
     * Устанавливает новый язык через enum
     */
    public void setLanguage(Language lang) {
        setLocale(lang.getCode());
    }

    /**
     * Возвращает текущий язык
     */
    public Language getLanguage() {
        return currentLocale.getLanguage().equals("en") ? Language.ENGLISH : Language.RUSSIAN;
    }

    /**
     * Перечисление поддерживаемых языков
     */
    public enum Language {
        RUSSIAN("ru", "Русский"),
        ENGLISH("en", "English");

        private final String code;
        private final String displayName;

        Language(String code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        public String getCode() {
            return code;
        }
    }
}