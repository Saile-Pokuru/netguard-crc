package gui;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {
    private final JTextArea area = new JTextArea();

    public LogPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.CARD);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Theme.CARD);
        area.setForeground(Theme.TEXT);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setMargin(new Insets(8, 10, 8, 10));
        area.setText("NetGuard CRC ready. Generate a codeword to begin.\n");
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.PRIMARY),
                "Event Log", 0, 0, Theme.HEADER, Theme.TEXT));
        add(scroll, BorderLayout.CENTER);
    }

    public void log(String message) {
        area.append(message + "\n");
        area.setCaretPosition(area.getDocument().getLength());
    }

    public void clearLogs() { area.setText(""); }
}
