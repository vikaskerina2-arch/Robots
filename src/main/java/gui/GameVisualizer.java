package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Представление робота. Только отрисовывает модель.
 * НЕ обрабатывает клики мыши — это делает контроллер.
 */
public class GameVisualizer extends JPanel implements PropertyChangeListener {

    private RobotModel model;

    /**
     * модель робота
     */
    public GameVisualizer(RobotModel model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);

        setDoubleBuffered(true);
    }

    /**
     * Устанавливает слушатель мыши от контроллера
     */
    public void setMouseListener(MouseListener listener) {
        addMouseListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d);
        drawTarget(g2d);
    }

    /**
     * Рисуем робота
     */
    private void drawRobot(Graphics2D g) {
        int x = (int) Math.round(model.getX());
        int y = (int) Math.round(model.getY());
        double direction = model.getDirection();

        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);

        g.setTransform(new AffineTransform());
    }

    /**
     * Рисуем цель
     */
    private void drawTarget(Graphics2D g) {
        int x = (int) Math.round(model.getTargetX());
        int y = (int) Math.round(model.getTargetY());

        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    /**
     * Рисуем закрашенный овал
     */
    private void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисуем контур овала
     */
    private void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}