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
    /**
     * Панель для внутренних окон
     */
    private final JDesktopPane desktopPane = new JDesktopPane();

    private LogWindow logWindow;
    private GameWindow gameWindow;

    private final FileSave storage = new FileSave("shkerina");

    /**
     * Создание главного окна
     * Размеры, окна лога и игры, меню
     */
    public MainApplicationFrame() {
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
        Logger.debug("Протокол работает");
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
        //русский
        String[] options = {"Да", "Нет"};
        int result = JOptionPane.showOptionDialog(
                this,
                "Вы хотите выйти?",
                "Подтверждение выхода",
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
        return WindowState.saveFrame(this, getPrefix());
    }

    @Override
    public void restoreState(Map<String, String> state) {
        WindowState.restoreFrame(this, state, getPrefix());
    }

    @Override
    public String getPrefix() {
        return "main";
    }
}
