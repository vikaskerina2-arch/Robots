package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Окно поле с роботом
 */
public class GameWindow extends JInternalFrame implements Save
{
    private final GameVisualizer gameVisualizer;

    /**
     * Новое окно поля игры с визуализатором в центре
     */
    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        gameVisualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public Map<String, String> saveState() {
        return WindowState.saveInternalFrame(this, getPrefix());
    }

    @Override
    public void restoreState(Map<String, String> state) {
        WindowState.restoreInternalFrame(this, state, getPrefix());
    }

    @Override
    public String getPrefix() {
        return "game";
    }
}
