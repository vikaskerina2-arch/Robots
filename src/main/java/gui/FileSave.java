package gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Класс для сохранения и загрузки состояния окон в файл
 */
public class FileSave {

    private final String path;  /** Полный путь к файлу конфигурации */

    /**
     * Создает объект для работы с файлом состояния
     * @param surname
     */
    public FileSave(String surname) {
        this.path = System.getProperty("user.home") + "/" + surname + "/state.cfg";
        new File(path).getParentFile().mkdirs();
    }

    /**
     * Сохраняет словарь с данными в файл
     * @param data
     */
    public void save(Map<String, String> data) {
        Properties props = new Properties();
        props.putAll(data);
        try (FileOutputStream out = new FileOutputStream(path)) {
            props.store(out, "состояние окон");
        } catch (IOException e) {}
    }

    /**
     * Загружает данные из файла в словарь
     * @return
     */
    public Map<String, String> load() {
        Map<String, String> data = new HashMap<>();
        File file = new File(path);
        if (!file.exists()) return data;

        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        } catch (IOException e) {
            return data;
        }

        for (String key : props.stringPropertyNames()) {
            data.put(key, props.getProperty(key));
        }
        return data;
    }
}
