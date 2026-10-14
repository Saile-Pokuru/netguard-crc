package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private final JLabel sentValue = value("0");
    private final JLabel receivedValue = value("0");
    private final JLabel errorsValue = value("0");
    private final JLabel retransValue = value("0");
    private final JLabel successValue = value("0%");

    public StatisticsPanel() {
        setLayout(new GridLayout(5, 2, 8, 14));
        setBackground(Theme.CARD);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.SUCCESS, 2),
                "Transmission Statistics", TitledBorder.LEFT, TitledBorder.TOP,
                Theme.HEADER, Theme.TEXT));
        add(label("Packets Sent")); add(sentValue);
        add(label("Packets Received")); add(receivedValue);
        add(label("CRC Errors")); add(errorsValue);
        add(label("Retransmissions")); add(retransValue);
        add(label("Delivery Rate")); add(successValue);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text); l.setForeground(Theme.TEXT); l.setFont(Theme.BODY); return l;
    }
    private static JLabel value(String text) {
        JLabel l = new JLabel(text); l.setForeground(Theme.SUCCESS);
        l.setFont(new Font("SansSerif", Font.BOLD, 17)); return l;
    }
    public void setPacketsSent(int v) { sentValue.setText(String.valueOf(v)); }
    public void setPacketsReceived(int v) { receivedValue.setText(String.valueOf(v)); }
    public void setCRCErrors(int v) { errorsValue.setText(String.valueOf(v)); }
    public void setRetransmissions(int v) { retransValue.setText(String.valueOf(v)); }
    public void setSuccessRate(double v) { successValue.setText(String.format("%.1f%%", v)); }
    public void reset() {
        setPacketsSent(0); setPacketsReceived(0); setCRCErrors(0);
        setRetransmissions(0); setSuccessRate(0);
    }
}
