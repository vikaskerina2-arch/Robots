package gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Модель робота. Состояние, управление
 */
public class RobotModel {

    // Состояние робота
    private double x = 100;
    private double y = 100;
    private double direction = 0;
    private double targetX = 150;
    private double targetY = 100;

    // Константы движения
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.01;

    // Для оповещения слушателей
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // Имена свойств для событий
    public final String PROP_X = "x";
    public final String PROP_Y = "y";
    public final String PROP_DIRECTION = "direction";
    public final String PROP_TARGET_X = "targetX";
    public final String PROP_TARGET_Y = "targetY";

    // Геттеры
    public double getX() { return x; }
    public double getY() { return y; }
    public double getDirection() { return direction; }
    public double getTargetX() { return targetX; }
    public double getTargetY() { return targetY; }

    /**
     * Угол до цели в радианах
     */
    public double getAngleToTarget() {
        double dx = targetX - x;
        double dy = targetY - y;
        return Math.atan2(dy, dx);
    }

    /**
     * Разница углов до цели и текущим в радианах.
     * Диапазон [-П, П]
     */
    public double getAngleDiff() {
        double angleToTarget = getAngleToTarget();
        double diff = angleToTarget - direction;
        return normalizeAngleDifference(diff);
    }

    // Сеттеры с оповещением
    public void setX(double x) {
        double old = this.x;
        this.x = x;
        pcs.firePropertyChange(PROP_X, old, x);
    }

    public void setY(double y) {
        double old = this.y;
        this.y = y;
        pcs.firePropertyChange(PROP_Y, old, y);
    }

    public void setDirection(double direction) {
        double old = this.direction;
        this.direction = normalizeRadians(direction);
        pcs.firePropertyChange(PROP_DIRECTION, old, this.direction);
    }

    // Цель с оповещением
    public void setTarget(double x, double y) {
        double oldX = this.targetX;
        double oldY = this.targetY;
        this.targetX = x;
        this.targetY = y;
        pcs.firePropertyChange(PROP_TARGET_X, oldX, x);
        pcs.firePropertyChange(PROP_TARGET_Y, oldY, y);
    }

    // Регистрация слушателей
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }


    /**
     * Обновление состояния 10 мс
     * @param durationMs
     */
    public void update(int durationMs) {
        double distance = distanceToTarget();
        //Уже на месте
        if (distance < 1.0) {
            return;
        }

        double angleDiff = getAngleDiff();

        // Поворот
        if (Math.abs(angleDiff) > 0.05) {
            double angularVelocity = (angleDiff > 0) ? MAX_ANGULAR_VELOCITY : -MAX_ANGULAR_VELOCITY;
            double step = angularVelocity * durationMs;
            if (Math.abs(step) > Math.abs(angleDiff)) {
                step = angleDiff;
            }
            double newDirection = direction + step;
            setDirection(newDirection);
        } else { // прямо
            double velocity = MAX_VELOCITY;
            double moveDistance = velocity * durationMs;

            if (distance < moveDistance){
                setX(targetX);
                setY(targetY);
            } else {
                double newX = x + moveDistance * Math.cos(direction);
                double newY = y + moveDistance * Math.sin(direction);
                setX(newX);
                setY(newY);
            }
        }
    }

    /**
     * Расстояние до цели
     */
    private double distanceToTarget() {
        double dx = targetX - x;
        double dy = targetY - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Нормализация угла [0, 2п)
     */
    private double normalizeRadians(double angle) {
        angle = angle % (2 * Math.PI);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    /**
     * Нормализация разницы углов [-П, П]
     */
    private double normalizeAngleDifference(double angle) {
        while (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        while (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        }
        return angle;
    }
}