package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Окно поле с роботом
 */
public class GameWindow extends JInternalFrame
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
}
