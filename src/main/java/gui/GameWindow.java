package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Окно игры с роботом
 */
public class GameWindow extends JInternalFrame implements Save {

    private final WindowState windowState = new WindowState();
    private final RobotModel model;
    private final GameVisualizer visualizer;
    private final Timer timer;

    /**
     * Создаем окно, модель и таймер
     */
    public GameWindow() {
        super("Игровое поле", true, true, true, true);

        model = new RobotModel();
        visualizer = new GameVisualizer(model);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        // Таймер для обновления модели
        timer = new Timer("robot-updater", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.update(10);
            }
        }, 0, 10);
    }

    /**
     * Возврат робота
     */
    public RobotModel getModel() {
        return model;
    }

    @Override
    public Map<String, String> saveState() {
        return windowState.saveInternalFrame(this, getPrefix());
    }

    @Override
    public void restoreState(Map<String, String> state) {
        windowState.restoreInternalFrame(this, state, getPrefix());
    }

    @Override
    public String getPrefix() {
        return "game";
    }
}