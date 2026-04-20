package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Окно игры с роботом и контроллер
 * Обработка кликов
 */
public class GameWindow extends JInternalFrame implements Save {

    private final WindowState windowState = new WindowState();
    private final RobotModel model;
    private final GameVisualizer visualizer;
    private final Controller controller;

    /**
     * Создаем окно, модель и таймер
     */
    public GameWindow() {
        super(Localization.getInstance().get("game.window"), true, true, true, true);

        model = new RobotModel();
        visualizer = new GameVisualizer(model);
        controller = new Controller(model, visualizer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void updateTitle() {
        setTitle(Localization.getInstance().get("game.window"));
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