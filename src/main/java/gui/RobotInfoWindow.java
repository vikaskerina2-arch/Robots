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
    private final Localization localization = Localization.getInstance();
    private final RobotModel model;
    private final JLabel xLabel;
    private final JLabel yLabel;
    private final JLabel directionLabel;
    private final JLabel targetAngleLabel;
    private final JLabel angleDiffLabel;

    private JLabel xTitleLabel;
    private JLabel yTitleLabel;
    private JLabel dirTitleLabel;
    private JLabel angleTitleLabel;
    private JLabel diffTitleLabel;

    /**
     * Создание окна
     */
    public RobotInfoWindow(RobotModel model) {
        super(Localization.getInstance().getString("robot.info"), true, true, true, true);
        this.model = model;
        this.model.addPropertyChangeListener(this); // подписываемся на модель

        setSize(350, 180);

        JPanel panel = new JPanel(new GridLayout(6, 2, 15, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Робот
        xTitleLabel = new JLabel();
        panel.add(xTitleLabel);
        xLabel = new JLabel(formatValue(model.getX()));
        panel.add(xLabel);

        yTitleLabel = new JLabel();
        panel.add(yTitleLabel);
        yLabel = new JLabel(formatValue(model.getY()));
        panel.add(yLabel);

        dirTitleLabel = new JLabel();
        panel.add(dirTitleLabel);
        directionLabel = new JLabel(formatAngle(model.getDirection()));
        panel.add(directionLabel);

        //Угол поворота
        angleTitleLabel = new JLabel();
        panel.add(angleTitleLabel);
        targetAngleLabel = new JLabel(formatRadians(model.getAngleToTarget()));
        panel.add(targetAngleLabel);

        //Угол до цели
        diffTitleLabel = new JLabel();
        panel.add(diffTitleLabel);
        angleDiffLabel = new JLabel(formatRadians(model.getAngleDiff()));
        panel.add(angleDiffLabel);

        getContentPane().add(panel);
        pack();

        updateTextsLang();
    }

    public void updateTexts() {
        updateTextsLang();
    }

    private String formatValue(double value) {
        return String.format("%.1f", value);
    }

    private String formatAngle(double angle) {
        return String.format("%.3f", angle);
    }

    private String formatRadians(double rad) {
        return String.format("%.3f", rad);
    }

    //Обновляет отображение при изменении модели
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            xLabel.setText(formatValue(model.getX()));
            yLabel.setText(formatValue(model.getY()));
            directionLabel.setText(formatAngle(model.getDirection()));
            targetAngleLabel.setText(formatRadians(model.getAngleToTarget()));
            angleDiffLabel.setText(formatRadians(model.getAngleDiff()));
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

    @Override
    public void updateTextsLang() {
        setTitle(localization.getString("robot.info"));
        xTitleLabel.setText(localization.getString("robot.position.x"));
        yTitleLabel.setText(localization.getString("robot.position.y"));
        dirTitleLabel.setText(localization.getString("robot.direction"));
        angleTitleLabel.setText(localization.getString("robot.angle.to.target"));
        diffTitleLabel.setText(localization.getString("robot.angle.diff"));
    }
}