package gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилитный класс для сохранения и восстановления состояния окон
 */
public class WindowState {

    /**
     * Сохраняет состояние обычного окна (Window/Frame)
     * @param window
     * @param prefix
     * @return
     */
    public Map<String, String> saveFrame(Window window, String prefix) {
        Map<String, String> state = new HashMap<>();
        state.put(prefix + ".x", String.valueOf(window.getX()));
        state.put(prefix + ".y", String.valueOf(window.getY()));
        state.put(prefix + ".width", String.valueOf(window.getWidth()));
        state.put(prefix + ".height", String.valueOf(window.getHeight()));

        // для главного окна сохраняем расширенное состояние
        if (window instanceof Frame) {
            state.put(prefix + ".extendedState", String.valueOf(((Frame) window).getExtendedState()));
        }

        return state;
    }

    /**
     * Сохраняет состояние внутреннего окна (JInternalFrame)
     * @param frame
     * @param prefix
     * @return
     */
    public Map<String, String> saveInternalFrame(JInternalFrame frame, String prefix) {
        Map<String, String> state = new HashMap<>();
        state.put(prefix + ".x", String.valueOf(frame.getX()));
        state.put(prefix + ".y", String.valueOf(frame.getY()));
        state.put(prefix + ".width", String.valueOf(frame.getWidth()));
        state.put(prefix + ".height", String.valueOf(frame.getHeight()));

        // ЗАПОМИНАЕМ, СВЕРНУТО ЛИ ОКНО
        state.put(prefix + ".icon", String.valueOf(frame.isIcon()));

        return state;
    }

    /**
     * Восстанавливает внутреннее окно
     * @param frame
     * @param state
     * @param prefix
     */
    public void restoreInternalFrame(JInternalFrame frame, Map<String, String> state, String prefix) {
        try {
            String x = state.get(prefix + ".x");
            String y = state.get(prefix + ".y");
            String w = state.get(prefix + ".width");
            String h = state.get(prefix + ".height");
            String icon = state.get(prefix + ".icon");

            if (x != null && y != null) {
                frame.setLocation(Integer.parseInt(x), Integer.parseInt(y));
            }
            if (w != null && h != null) {
                frame.setSize(Integer.parseInt(w), Integer.parseInt(h));
            }

            // ВОССТАНАВЛИВАЕМ СВЕРНУТОЕ СОСТОЯНИЕ
            if (icon != null) {
                boolean isIcon = Boolean.parseBoolean(icon);
                if (isIcon) {
                    try {
                        frame.setIcon(true);
                    } catch (Exception e) {}
                }
            }

        } catch (NumberFormatException e) {}
    }

    /**
     * Восстанавливает обычное окно
     * @param window
     * @param state
     * @param prefix
     */
    public void restoreFrame(Window window, Map<String, String> state, String prefix) {
        try {
            String x = state.get(prefix + ".x");
            String y = state.get(prefix + ".y");
            String w = state.get(prefix + ".width");
            String h = state.get(prefix + ".height");

            if (x != null && y != null && w != null && h != null) {
                window.setBounds(
                        Integer.parseInt(x),
                        Integer.parseInt(y),
                        Integer.parseInt(w),
                        Integer.parseInt(h)
                );
            }

            if (window instanceof Frame) {
                String extState = state.get(prefix + ".extendedState");
                if (extState != null) {
                    ((Frame) window).setExtendedState(Integer.parseInt(extState));
                }
            }
        } catch (NumberFormatException e) {}
    }
}

