package gui;

import log.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * Класс создания и настройки меню
 */
public class MenuBuilder {
    private final MainApplicationFrame frame;

    /**
     * @param frame ссылка на главное окно для вызова методов (setLookAndFeel)
     */
    public MenuBuilder(MainApplicationFrame frame){
        this.frame = frame;
    }

    /**
     * Создание строки меню
     */
    public JMenuBar buildMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        return menuBar;
    }

    /**
     * Создание меню режима отображения
     */
    private JMenu createLookAndFeelMenu(){
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        lookAndFeelMenu.add(createSystemLookAndFeelItem());
        lookAndFeelMenu.add(createCrossPlatformLookAndFeelItem());
        return lookAndFeelMenu;
    }

    /**
     * Создание меню для схемы системы
     */
    private JMenuItem createSystemLookAndFeelItem() {
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame.invalidate();
        });
        return systemLookAndFeel;
    }

    /**
     * Создание меню универсальной схемы
     */
    private JMenuItem createCrossPlatformLookAndFeelItem() {
        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            frame.invalidate();
        });
        return crossplatformLookAndFeel;
    }

    /**
     * Создание меню с текстовыми командами
     */
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        testMenu.add(createAddLogMessageItem());

        return testMenu;
    }

    /**
     * Создание пункта для добавления сообщений в лог
     */
    private JMenuItem createAddLogMessageItem() {
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        return addLogMessageItem;
    }

    /**
     * Создание меню выхода
     */
    private JMenu createExitMenu(){
        JMenu exitMenu = new JMenu("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_X);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Завершение работы приложения");
        exitMenu.add(createExitMenuItem());
        return exitMenu;
    }

    /**
     * Создание пункта меню вызода
     */
    private JMenuItem createExitMenuItem(){
        JMenuItem exitItem = new JMenuItem("Завершить работу", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_DOWN_MASK));
        exitItem.addActionListener((event)->{
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        return exitItem;
    }
}
