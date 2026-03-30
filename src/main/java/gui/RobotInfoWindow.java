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
    private final JLabel targetAngleLabel;
    private final JLabel angleDiffLabel;

    /**
     * Создание окна
     */
    public RobotInfoWindow(RobotModel model) {
        super("Информация о роботе", true, true, true, true);
        this.model = model;
        this.model.addPropertyChangeListener(this); // подписываемся на модель

        setSize(250, 180);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Робот
        panel.add(new JLabel("Позиция X:"));
        xLabel = new JLabel(String.format("%.1f", model.getX()));
        panel.add(xLabel);

        panel.add(new JLabel("Позиция Y:"));
        yLabel = new JLabel(String.format("%.1f", model.getY()));
        panel.add(yLabel);

        panel.add(new JLabel("Направление:"));
        directionLabel = new JLabel(String.format("%.1f°", Math.toDegrees(model.getDirection())));
        panel.add(directionLabel);

        //Угол поворота
        panel.add(new JLabel("Угол поворота(радианы):"));
        targetAngleLabel = new JLabel(String.format("%.3f", model.getAngleToTarget()));
        panel.add(targetAngleLabel);

        //Угол до цели
        panel.add(new JLabel("Угол до цели(радианы):"));
        angleDiffLabel = new JLabel(String.format("%.3f", model.getAngleDiff()));
        panel.add(angleDiffLabel);

        getContentPane().add(panel);
        pack();
    }

    //Обновляет отображение при изменении модели
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            xLabel.setText(String.format("%.1f", model.getX()));
            yLabel.setText(String.format("%.1f", model.getY()));
            directionLabel.setText(String.format("%.3f", model.getDirection()));
            targetAngleLabel.setText(String.format("%.3f", model.getAngleToTarget()));
            angleDiffLabel.setText(String.format("%.3f", model.getAngleDiff()));
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