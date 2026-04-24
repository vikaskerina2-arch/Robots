package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Окно приложения с интерфейсом MDI
 * Размещение внутренних окон, создание окон лога и игры
 */
public class MainApplicationFrame extends JFrame implements Save
{
    private final WindowState windowState = new WindowState();
    private final Localization localization = Localization.getInstance();
    /**
     * Панель для внутренних окон
     */
    private final JDesktopPane desktopPane = new JDesktopPane();

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private RobotInfoWindow infoWindow;

    private final FileSave storage = new FileSave("shkerina");

    /**
     * Создание главного окна
     * Размеры, окна лога и игры, меню
     */
    public MainApplicationFrame() {
        loadLanguageState();
        //Отступы
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        //область работы
        setContentPane(desktopPane);

        //создание окна лога
        logWindow = createLogWindow();
        addWindow(logWindow);

        //создание окна игры
        gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        // Новое окно с информацией о роботе
        infoWindow = new RobotInfoWindow(gameWindow.getModel());
        infoWindow.setSize(250, 180);
        infoWindow.setLocation(420, 10);
        addWindow(infoWindow);

        //создание строки меню
        MenuBuilder menuBuilder = new MenuBuilder(this);
        setJMenuBar(menuBuilder.buildMenuBar());

        // пробуем загрузить сохранённое состояние
        loadAllStates();

        //отключила закрытие обычное
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        //перехват закрытия окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                exitApplication();
            }
        });
        updateTextsLang();
    }

    /**
     * Загружаем язык из файла
     */
    private void loadLanguageState() {
        Map<String, String> all = storage.load();
        String saveCode = all.get("language");
        if (saveCode != null) {
            localization.setLocaleFromString(saveCode);
        }
    }

    /**
     * Сохраняем язык в файл
     */
    private void saveLanguageState() {
        Map<String, String> all = storage.load();
        all.put("language", localization.getLocaleString());
        storage.save(all);
    }

    public void switchLanguage(String langCode) {
        localization.setLocaleFromString(langCode);
        saveLanguageState();
        updateTextsLang();

        // Обновляем меню
        MenuBuilder menuBuilder = new MenuBuilder(this);
        setJMenuBar(menuBuilder.buildMenuBar());
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Загружает состояние всех окон из файла
     */
    private void loadAllStates() {
        Map<String, String> all = storage.load();
        if (all.isEmpty()) return;

        // восстанавливаем все окна
        restoreState(new PrefixMap(all, getPrefix()));
        logWindow.restoreState(new PrefixMap(all, logWindow.getPrefix()));
        gameWindow.restoreState(new PrefixMap(all, gameWindow.getPrefix()));
        infoWindow.restoreState(new PrefixMap(all, infoWindow.getPrefix()));
    }

    /**
     * Сохраняет состояние всех окон в файл
     */
    private void saveAllStates() {
        Map<String, String> all = new HashMap<>();

        // сохраняем все окна
        new PrefixMap(all, getPrefix()).putAll(saveState());
        new PrefixMap(all, logWindow.getPrefix()).putAll(logWindow.saveState());
        new PrefixMap(all, gameWindow.getPrefix()).putAll(gameWindow.saveState());
        new PrefixMap(all, infoWindow.getPrefix()).putAll(infoWindow.saveState());

        storage.save(all);
    }

    /**
     * Создание и настройка окна лога
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        logWindow.pack();
        Logger.debug(localization.getString("log.window.started"));
        return logWindow;
    }

    /**
     * Добавляем внутреннее окно на рабочий стол
     * @param frame внутреннее окно
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Выход с подтверждением
     */
    public void exitApplication(){
        String[] options = {localization.getString("confirm.exit.yes"),
                localization.getString("confirm.exit.no")
        };
        int result = JOptionPane.showOptionDialog(
                this,
                localization.getString("confirm.exit.message"),
                localization.getString("confirm.exit.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        if (result == JOptionPane.YES_OPTION){
            saveAllStates();
            dispose();
            System.exit(0);
        }
    }

    /**
     * Установка внешнего вида (Look and Feel)
     * Обновление компонентов, если ошибка - игнорируем
     *
     * @param className имя класса Look and Feel
     */
    void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            //игнорируем ошибки
        }
    }

    @Override
    public Map<String, String> saveState() {
        return windowState.saveFrame(this, getPrefix());
    }

    @Override
    public void restoreState(Map<String, String> state) {
        windowState.restoreFrame(this, state, getPrefix());
    }

    @Override
    public String getPrefix() {
        return "main";
    }

    @Override
    public void updateTextsLang() {
        setTitle(localization.getString("game.window"));
        if (logWindow != null) logWindow.updateTextsLang();
        if (gameWindow != null) gameWindow.updateTextsLang();
        if (infoWindow != null) infoWindow.updateTextsLang();
    }
}
