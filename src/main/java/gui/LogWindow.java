package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import java.awt.*;

/**
 * Окно для отображения лога - протокола работы
 * автоматическое обновление отображения
 */
public class LogWindow extends JInternalFrame implements LogChangeListener
{
    private LogWindowSource logSource;
    private TextArea logContent;

    /**
     * Новое окно протокола
     * @param logSource
     */
    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);
        this.logContent = new TextArea("");
        this.logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    /**
     * Обновление текста лога
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all())
        {
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
}
