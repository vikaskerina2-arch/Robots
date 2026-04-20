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
        menuBar.add(createLanguageMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        return menuBar;
    }

    /**
     * Язык
     */
    private JMenu createLanguageMenu() {
        JMenu languageMenu = new JMenu(Localization.getInstance().get("menu.language"));
        languageMenu.setMnemonic(KeyEvent.VK_L);

        JMenuItem russianItem = new JMenuItem(Localization.getInstance().get("menu.language.russian"));
        russianItem.addActionListener(e -> frame.switchLanguage(Localization.Language.RUSSIAN));

        JMenuItem englishItem = new JMenuItem(Localization.getInstance().get("menu.language.english"));
        englishItem.addActionListener(e -> frame.switchLanguage(Localization.Language.ENGLISH));

        languageMenu.add(russianItem);
        languageMenu.add(englishItem);

        return languageMenu;
    }

    /**
     * Создание меню режима отображения
     */
    private JMenu createLookAndFeelMenu(){
        JMenu lookAndFeelMenu = new JMenu(Localization.getInstance().get("menu.view"));
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
        JMenuItem systemLookAndFeel = new JMenuItem(Localization.getInstance().get("menu.view.system"), KeyEvent.VK_S);
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
        JMenuItem crossplatformLookAndFeel = new JMenuItem(Localization.getInstance().get("menu.view.universal"), KeyEvent.VK_S);
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
        JMenu testMenu = new JMenu(Localization.getInstance().get("menu.tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        testMenu.add(createAddLogMessageItem());
        testMenu.add(createAnotherLogMessageItem());

        return testMenu;
    }

    /**
     * Создание пункта для добавления сообщений в лог
     */
    private JMenuItem createAddLogMessageItem() {
        JMenuItem addLogMessageItem = new JMenuItem(Localization.getInstance().get("menu.tests.log"), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug(Localization.getInstance().get("log.message"));
        });
        return addLogMessageItem;
    }

    /**
     * Создание меню выхода
     */
    private JMenu createExitMenu(){
        JMenu exitMenu = new JMenu(Localization.getInstance().get("menu.exit"));
        exitMenu.setMnemonic(KeyEvent.VK_X);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Завершение работы приложения");
        exitMenu.add(createExitMenuItem());
        return exitMenu;
    }

    /**
     * Создание пункта меню выхода
     */
    private JMenuItem createExitMenuItem(){
        JMenuItem exitItem = new JMenuItem(Localization.getInstance().get("menu.exit.confirm"), KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_DOWN_MASK));
        exitItem.addActionListener((event)->{
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        return exitItem;
    }

    /**
     * Создание кнопки
     */
    private JMenuItem createAnotherLogMessageItem() {
        JMenuItem item = new JMenuItem(Localization.getInstance().get("menu.tests.log2"), KeyEvent.VK_M);
        item.addActionListener((event) -> {
            Logger.debug(Localization.getInstance().get("log.message2"));
        });
        return item;
    }
}
