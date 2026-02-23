package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Окно приложения с интерфейсом MDI
 * Размещение внутренних окон, создание окон лога и игры
 */
public class MainApplicationFrame extends JFrame
{
    /**
     * Панель для внутренних окон
     */
    private final JDesktopPane desktopPane = new JDesktopPane();

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
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        //создание окна игры
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        //создание строки меню
        MenuBuilder menuBuilder = new MenuBuilder(this);
        setJMenuBar(menuBuilder.buildMenuBar());

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
     * Создание и настройка окна лога
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
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
}
