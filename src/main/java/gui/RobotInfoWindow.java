package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Окно для координат робота
 */
public class RobotInfoWindow extends JInternalFrame implements Save, PropertyChangeListener {

    private final WindowState windowState = new WindowState();
    private final RobotModel model;
    private final JLabel xLabel;
    private final JLabel yLabel;
    private final JLabel directionLabel;
    private final JLabel targetXLabel;
    private final JLabel targetYLabel;

    /**
     * Создание окна
     */
    public RobotInfoWindow(RobotModel model) {
        super("Информация о роботе", true, true, true, true);
        this.model = model;
        this.model.addPropertyChangeListener(this); // подписываемся на модель

        setSize(250, 180);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Позиция X:"));
        xLabel = new JLabel(String.format("%.1f", model.getX()));
        panel.add(xLabel);

        panel.add(new JLabel("Позиция Y:"));
        yLabel = new JLabel(String.format("%.1f", model.getY()));
        panel.add(yLabel);

        panel.add(new JLabel("Направление:"));
        directionLabel = new JLabel(String.format("%.1f°", Math.toDegrees(model.getDirection())));
        panel.add(directionLabel);

        panel.add(new JLabel("Цель X:"));
        targetXLabel = new JLabel(String.format("%.1f", model.getTargetX()));
        panel.add(targetXLabel);

        panel.add(new JLabel("Цель Y:"));
        targetYLabel = new JLabel(String.format("%.1f", model.getTargetY()));
        panel.add(targetYLabel);

        getContentPane().add(panel);
        pack();
    }

    //обновляем при изменении модели
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            xLabel.setText(String.format("%.1f", model.getX()));
            yLabel.setText(String.format("%.1f", model.getY()));
            directionLabel.setText(String.format("%.1f°", Math.toDegrees(model.getDirection())));
            targetXLabel.setText(String.format("%.1f", model.getTargetX()));
            targetYLabel.setText(String.format("%.1f", model.getTargetY()));
        });
    }

    // Сохраняем состояние окна
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
        return "info";
    }
}