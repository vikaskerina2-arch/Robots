package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Окно для отображения лога
 * автоматическое обновление
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, Save
{
    private final WindowState windowState = new WindowState();
    private LogWindowSource logSource;
    private TextArea logContent;
    private final int maxVisible = 5;

    /**
     * Новое окно протокола
     */
    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logContent = new TextArea("");
        this.logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        this.logSource.registerListener(this);
        updateLogContent();
    }

    /**
     * Обновление текста лога
     */
    private void updateLogContent() {
        if (logSource == null) {
            return;
        }

        int totalSize = logSource.size();
        int startFrom = Math.max(0, totalSize - maxVisible);
        int count = totalSize - startFrom;

        List<LogEntry> entries = logSource.range(startFrom, count);
        StringBuilder content = new StringBuilder();

        for (LogEntry entry : entries) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
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
        return "log";
    }

    @Override
    public void dispose() {
        if (logSource != null) {
            logSource.unregisterListener(this);
        }
        super.dispose();
    }
}
