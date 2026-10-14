package gui;

import javax.swing.*;
import java.awt.*;

public class AnimationPanel extends JPanel {
    private int packetX = 100;
    private boolean transmitting;
    private boolean error;
    private final Timer timer;

    public AnimationPanel() {
        setBackground(Theme.BACKGROUND);
        setPreferredSize(new Dimension(480, 300));
        timer = new Timer(18, e -> {
            packetX += 5;
            if (packetX > Math.max(220, getWidth() - 120)) {
                transmitting = false;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
    }

    public void startTransmission() { start(false); }
    public void startErrorTransmission() { start(true); }
    private void start(boolean hasError) {
        packetX = 100; error = hasError; transmitting = true;
        timer.stop(); timer.start(); repaint();
    }
    public void resetAnimation() {
        timer.stop(); packetX = 100; transmitting = false; error = false; repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight(), y = h / 2 - 32;
        int sx = 20, rx = Math.max(270, w - 115);

        g2.setColor(Theme.MUTED);
        g2.setFont(Theme.HEADER);
        g2.drawString("TRANSMISSION PATH", 20, 28);
        g2.setColor(new Color(82, 99, 124));
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(sx + 88, y + 32, rx, y + 32);

        g2.setColor(Theme.SUCCESS);
        g2.fillRoundRect(sx, y, 88, 64, 16, 16);
        g2.setColor(Color.WHITE);
        g2.drawString("Sender", sx + 17, y + 36);

        g2.setColor(Theme.PRIMARY);
        g2.fillRoundRect(rx, y, 92, 64, 16, 16);
        g2.setColor(Color.WHITE);
        g2.drawString("Receiver", rx + 13, y + 36);

        if (transmitting) {
            g2.setColor(Theme.WARNING);
            g2.fillRoundRect(packetX, y + 18, 58, 28, 8, 8);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString("PACKET", packetX + 7, y + 36);
            if (error && packetX > (sx + rx) / 2 - 20) {
                g2.setColor(Theme.DANGER);
                g2.setFont(new Font("SansSerif", Font.BOLD, 24));
                g2.drawString("×", (sx + rx) / 2, y - 8);
                g2.setFont(Theme.BODY);
                g2.drawString("CRC error", (sx + rx) / 2 - 28, y + 83);
            }
        } else {
            g2.setColor(Theme.MUTED);
            g2.setFont(Theme.BODY);
            g2.drawString("Generate a CRC codeword, then transmit or verify it.", 20, h - 25);
        }
        g2.dispose();
    }
}
