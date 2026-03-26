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
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

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
     * Обновление состояния 10мс
     * @param durationMs
     */
    public void update(int durationMs) {
        double distance = distanceToTarget();
        if (distance < 0.5) return;

        double angleToTarget = angleToTarget();
        double angularVelocity = 0;
        double angleDiff = normalizeRadians(angleToTarget - direction);

        if (angleDiff > 0 && angleDiff < Math.PI) {
            angularVelocity = MAX_ANGULAR_VELOCITY;
        } else if (angleDiff < 0 && angleDiff > -Math.PI) {
            angularVelocity = -MAX_ANGULAR_VELOCITY;
        } else if (angleDiff > Math.PI) {
            angularVelocity = -MAX_ANGULAR_VELOCITY;
        } else if (angleDiff < -Math.PI) {
            angularVelocity = MAX_ANGULAR_VELOCITY;
        }

        moveRobot(MAX_VELOCITY, angularVelocity, durationMs);
    }

    /**
     * Перемещение по формулам
     * @param velocity
     * @param angularVelocity
     * @param duration
     */
    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = Math.max(0, Math.min(velocity, MAX_VELOCITY));
        angularVelocity = Math.max(-MAX_ANGULAR_VELOCITY, Math.min(angularVelocity, MAX_ANGULAR_VELOCITY));

        double newX = x;
        double newY = y;
        double newDirection = direction;

        if (Math.abs(angularVelocity) < 1e-10) {
            //прямо
            newX = x + velocity * duration * Math.cos(direction);
            newY = y + velocity * duration * Math.sin(direction);
            newDirection = direction;
        } else {
            //дуга
            double radius = velocity / angularVelocity;
            double deltaAngle = angularVelocity * duration;
            newX = x + radius * (Math.sin(direction + deltaAngle) - Math.sin(direction));
            newY = y - radius * (Math.cos(direction + deltaAngle) - Math.cos(direction));
            newDirection = direction + deltaAngle;
        }

        setX(newX);
        setY(newY);
        setDirection(newDirection);
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
     * Угол до цели
     */
    private double angleToTarget() {
        double dx = targetX - x;
        double dy = targetY - y;
        return Math.atan2(dy, dx);
    }

    /**
     * Нормализация угла
     */
    private double normalizeRadians(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }
}